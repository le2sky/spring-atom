package coupon.domain;

public class CouponFullAmountStrategy implements CouponBenefitStrategy {

    @Override
    public String getStrategyName() {
        return "FULL";
    }

    @Override
    public Double calculateBenefit(Coupon coupon) {
        return coupon.getMoney();
    }
}
