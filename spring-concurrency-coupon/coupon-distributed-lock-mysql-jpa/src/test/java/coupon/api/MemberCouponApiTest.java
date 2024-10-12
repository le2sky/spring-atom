package coupon.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberCouponApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Test
    @DisplayName("한 사용자는 이미 발급한 쿠폰을 또 발급할 수 없다.")
    void alreadyIssued() throws InterruptedException {
        Member member = memberRepository.save(createMember());
        Coupon coupon = couponRepository.save(createCoupon());
        IssueCouponRequest request = new IssueCouponRequest(member.getId(), coupon.getId());
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    mockMvc.perform(post("/member-coupon")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    );
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        List<MemberCoupon> result = memberCouponRepository.findAll();
        assertThat(result).hasSize(1);
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
}
