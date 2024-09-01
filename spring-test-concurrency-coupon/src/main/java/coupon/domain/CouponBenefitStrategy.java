package coupon.domain;

public interface CouponBenefitStrategy {

    String getStrategyName();

    Benefit calculteBenefit(Coupon coupon);
}
