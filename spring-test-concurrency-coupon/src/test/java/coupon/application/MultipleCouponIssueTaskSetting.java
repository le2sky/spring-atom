package coupon.application;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import coupon.domain.Coupon;
import coupon.domain.Member;
import coupon.domain.MemberRepository;

// 스레드 풀에 쿠폰 동시 발행 테스크를 설정한다.
class MultipleCouponIssueTaskSetting implements CouponConcurrencyTaskSetting {

    private final MemberRepository memberRepository;
    private final MemberCouponService memberCouponService;

    public MultipleCouponIssueTaskSetting(
            MemberRepository memberRepository,
            MemberCouponService memberCouponService
    ) {
        this.memberRepository = memberRepository;
        this.memberCouponService = memberCouponService;
    }

    @Override
    public void initializeTask(
            int threadCount,
            ExecutorService executorService,
            CountDownLatch finishLatch,
            CountDownLatch startLatch,
            Coupon coupon
    ) {

        for (int i = 0; i < threadCount; i++) {
            Member member = memberRepository.save(new Member(null, "test-user-" + i));

            executorService.submit(() -> {
                try {
                    startLatch.await();
                    memberCouponService.issue(member.getId(), coupon.getId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
    }
}
