package coupon.application;

public interface MemberCouponIssueLock {

    void lock(Long memberId, Long couponId);

    void unlock(Long memberId, Long couponId);
}
