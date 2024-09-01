package coupon.domain;

import java.time.LocalDateTime;

class CouponPeriodAmountStrategy implements CouponBenefitStrategy {

    private static final int WEEK_PER_PERCENT = 10;

    @Override
    public String getStrategyName() {
        return "PERIOD";
    }

    @Override
    public Double calculateBenefit(Coupon coupon) {
        int weeks = calculateWeek(coupon);
        double minusMoney = (coupon.getMoney() / WEEK_PER_PERCENT) * weeks;
        return coupon.getMoney() - minusMoney;
    }

    private int calculateWeek(Coupon coupon) {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime issuedAt = coupon.getIssuedAt();

        int weeks = 0;
        for (int n = 1; n <= WEEK_PER_PERCENT; n++) {
            LocalDateTime base = issuedAt.plusWeeks(n);
            if (nowDate.isBefore(base)) {
                break;
            }

            weeks++;
        }

        return weeks;
    }
}
