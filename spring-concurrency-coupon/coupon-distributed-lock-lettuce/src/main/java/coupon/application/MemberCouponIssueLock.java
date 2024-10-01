package coupon.application;

public interface MemberCouponIssueLock {

    boolean lock(Long memberId, Long couponId);

    void unlock(Long memberId, Long couponId);
}
