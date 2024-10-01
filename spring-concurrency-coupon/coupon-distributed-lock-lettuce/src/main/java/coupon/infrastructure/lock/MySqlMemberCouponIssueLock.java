package coupon.infrastructure.lock;

import coupon.application.MemberCouponIssueLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MySqlMemberCouponIssueLock implements MemberCouponIssueLock {

    private final MySqlLockRepository mySqlLockRepository;

    @Override
    public void lock(Long memberId, Long couponId) {
        mySqlLockRepository.getLock("memberCoupon-" + memberId.toString() + couponId.toString());
    }

    @Override
    public void unlock(Long memberId, Long couponId) {
        mySqlLockRepository.releaseLock("memberCoupon-" + memberId.toString() + couponId.toString());
    }
}
