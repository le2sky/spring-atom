package coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CouponPeriodAmountStrategyTest {

    @ParameterizedTest
    @CsvSource(
            value = {
                    "1000.0:1:900",
                    "1000.0:9:100",
                    "400.0:4:240",
                    "400.0:10:0",
            },
            delimiter = ':'
    )
    @DisplayName("쿠폰 생성일로부터 일주일이 지날때마다 전체 금액에 10% 차감된다.")
    void calculate(Double money, int week, Double expected) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeWeek = now.minusWeeks(week);
        Coupon coupon = createCoupon(money, beforeWeek);
        CouponPeriodAmountStrategy couponPeriodAmountStrategy = new CouponPeriodAmountStrategy();

        Double result = couponPeriodAmountStrategy.calculateBenefit(coupon);

        assertThat(result).isEqualTo(expected);
    }

    private Coupon createCoupon(Double money, LocalDateTime beforeWeek) {
        return new Coupon(
                null,
                "sample",
                money,
                null,
                beforeWeek
        );
    }
}
