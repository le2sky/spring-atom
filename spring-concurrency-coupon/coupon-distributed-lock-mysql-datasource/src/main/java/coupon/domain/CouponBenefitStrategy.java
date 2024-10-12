package coupon.domain;

public interface CouponBenefitStrategy {

    String getStrategyName();

    Double calculateBenefit(Coupon coupon);
}
