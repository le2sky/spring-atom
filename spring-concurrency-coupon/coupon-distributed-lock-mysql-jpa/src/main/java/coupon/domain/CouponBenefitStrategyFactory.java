package coupon.domain;

import java.util.HashMap;
import java.util.Map;

class CouponBenefitStrategyFactory {

    private static final Map<String, CouponBenefitStrategy> classifier = new HashMap<>();

    static {
        CouponBenefitStrategy fullAmountStrategy = new CouponFullAmountStrategy();
        CouponBenefitStrategy periodAmountStrategy = new CouponPeriodAmountStrategy();
        classifier.put(fullAmountStrategy.getStrategyName(), fullAmountStrategy);
        classifier.put(periodAmountStrategy.getStrategyName(), periodAmountStrategy);
    }

    public static CouponBenefitStrategy generate(String type) {
        return classifier.get(type);
    }
}
