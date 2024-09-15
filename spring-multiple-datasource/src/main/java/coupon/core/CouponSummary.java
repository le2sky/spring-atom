package coupon.core;

record CouponSummary(Long couponId, Long remaining) {

    public CouponSummary(Long couponId, Long issueCount, Long issueLimit) {
        this(couponId, issueLimit - issueCount);
    }
}
