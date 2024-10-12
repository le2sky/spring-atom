package coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import coupon.domain.Benefit;
import coupon.domain.BenefitRepository;
import coupon.domain.Coupon;
import coupon.domain.CouponFullAmountStrategy;
import coupon.domain.CouponRepository;
import coupon.domain.Member;
import coupon.domain.MemberCoupon;
import coupon.domain.MemberCouponRepository;
import coupon.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 동시성 테스트에서 각 스레드 별로 신규 영속성 컨텍스트가 생성되기 때문에,
 * `@Transactional` 을 이용하면 insert 쿼리가 커밋되기 이전에 롤백되어 각 스레드에서 member, coupon 조회가 불가하다.
 * 따라서, 테스트 레벨에서 격리를 지원하지 않도록 구현했다.
 */
@SpringBootTest
class MemberCouponServiceTest {

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private BenefitRepository benefitRepository;


    @Test
    @DisplayName("사용자가 이미 해당 쿠폰을 발급하면 더 이상 발급 받을 수 없다.")
    void checkMultiIssue() {
        Coupon coupon = couponRepository.save(createCoupon());
        Member member = memberRepository.save(createMember());
        memberCouponService.issue(member.getId(), coupon.getId());

        assertThatThrownBy(() -> memberCouponService.issue(member.getId(), coupon.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 사용자는 이미 쿠폰을 발급했습니다.");
    }

    @Test
    @DisplayName("스레드 100개 동시 쿠폰 발급 요청 - 같은 사용자 발급 1개 제한")
    void multipleIssueCoupon() throws InterruptedException {
        int threadCount = 100;
        Coupon coupon = couponRepository.save(createCoupon());

        controlMultiThread(threadCount, coupon, new MultipleCouponIssueTaskSetting(memberRepository, memberCouponService));

        List<MemberCoupon> results = memberCouponRepository.findAll();
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("스레드 100개 동시 사용자 쿠폰 교환 요청")
    void multipleExchangeCoupon() throws InterruptedException {
        int threadCount = 100;
        Coupon coupon = couponRepository.save(createCoupon());

        controlMultiThread(threadCount, coupon, new MultipleCouponExchangeTaskSetting(memberRepository, memberCouponService));

        List<Benefit> results = benefitRepository.findAll();
        assertThat(results).hasSize(1);
    }

    private Coupon createCoupon() {
        return new Coupon(
                null,
                "5,000원 교환권",
                5000.0,
                new CouponFullAmountStrategy(),
                null
        );
    }

    private Member createMember() {
        return new Member(null, "atom");
    }

    // 스레드 풀 생성 및 스레드 대기, 실행 제어
    private void controlMultiThread(
            int threadCount,
            Coupon coupon,
            CouponConcurrencyTaskSetting setting
    ) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch finishLatch = new CountDownLatch(threadCount); // 모든 스레드가 작업을 마쳤을 때 카운트를 줄임
        CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 시작할 준비가 되었을 때 카운트를 줄임

        setting.initializeTask(threadCount, executorService, finishLatch, startLatch, coupon);

        startLatch.countDown();
        finishLatch.await();
    }
}
