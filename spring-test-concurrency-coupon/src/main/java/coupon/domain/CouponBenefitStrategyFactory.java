package coupon.domain;

import java.util.HashMap;
import java.util.Map;

class CouponBenefitStrategyFactory {

    private static Map<String, CouponBenefitStrategy> classfier = new HashMap<>();

    static {
        CouponBenefitStrategy fullAmountStrategy = new CouponFullAmountStrategy();
        CouponBenefitStrategy periodAmountStrategy = new CouponPeriodAmountStrategy();
        classfier.put(fullAmountStrategy.getStrategyName(), fullAmountStrategy);
        classfier.put(periodAmountStrategy.getStrategyName(), periodAmountStrategy);
    }

    public static CouponBenefitStrategy generate(String type) {
        return classfier.get(type);
    }
}
