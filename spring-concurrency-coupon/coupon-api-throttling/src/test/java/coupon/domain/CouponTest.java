package coupon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CouponTest {

    @Test
    @DisplayName("교환 금액을 계산한다.")
    void calculateBenefit() {
        Coupon coupon = createCoupon();

        Double result = coupon.calculateBenefit();

        assertThat(result).isEqualTo(1000.0);
    }

    private Coupon createCoupon() {
        return new Coupon(
                null,
                "coupon",
                1000.0,
                new CouponFullAmountStrategy(),
                LocalDateTime.now()
        );
    }
}
