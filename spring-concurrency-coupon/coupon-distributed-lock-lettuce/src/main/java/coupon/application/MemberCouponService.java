package coupon.application;

import coupon.domain.Benefit;
import coupon.domain.BenefitRepository;
import coupon.domain.MemberCoupon;
import coupon.domain.MemberCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponService {

    private final MemberCouponIssuer memberCouponIssuer;
    private final MemberCouponIssueLock memberCouponIssueLock;
    private final MemberCouponRepository memberCouponRepository;
    private final BenefitRepository benefitRepository;

    public Long issue(Long memberId, Long couponId) {
        try {
            spinLock(memberId, couponId);
            return memberCouponIssuer.issue(memberId, couponId);
        } finally {
            memberCouponIssueLock.unlock(memberId, couponId);
        }
    }

    private void spinLock(Long memberId, Long couponId) {
        int tryCount = 10;

        while (!memberCouponIssueLock.lock(memberId, couponId)) {
            if (tryCount-- == 0) {
                // lock 획득 실패 처리
                throw new RuntimeException();
            }

            try {
                // redis에 너무 많은 부하를 주지 않기 위해 sleep을 설정
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    public void exchange(Long memberCouponId) {
        log.info("쿠폰 금액 교환 memberCouponId = {}", memberCouponId);

        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElseThrow();
        Benefit exchange = memberCoupon.exchange();
        benefitRepository.save(exchange);

        log.info("금액 교환 종료 memberCouponId = {}", memberCouponId);
    }
}
