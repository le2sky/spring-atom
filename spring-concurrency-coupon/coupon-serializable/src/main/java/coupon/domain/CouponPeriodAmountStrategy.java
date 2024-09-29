package coupon.domain;

import java.time.LocalDateTime;

public class CouponPeriodAmountStrategy implements CouponBenefitStrategy {

    private static final int PERCENT_PER_WEEK = 10;

    @Override
    public String getStrategyName() {
        return "PERIOD";
    }

    @Override
    public Double calculateBenefit(Coupon coupon) {
        int weeks = calculateWeek(coupon);
        double minusMoney = (coupon.getMoney() / PERCENT_PER_WEEK) * weeks;
        return coupon.getMoney() - minusMoney;
    }

    private int calculateWeek(Coupon coupon) {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime issuedAt = coupon.getIssuedAt();

        int weeks = 0;
        for (int n = 1; n <= PERCENT_PER_WEEK; n++) {
            LocalDateTime base = issuedAt.plusWeeks(n);
            if (nowDate.isBefore(base)) {
                break;
            }

            weeks++;
        }

        return weeks;
    }
}
