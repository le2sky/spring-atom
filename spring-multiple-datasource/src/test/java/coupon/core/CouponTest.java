package coupon.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    @DisplayName("쿠폰을 발행하면 발행 수가 증가한다.")
    void issue() {
        Coupon coupon = new Coupon(0L, 100L);

        coupon.issue();

        assertThat(coupon.getIssueCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("발행 한도에 도달하면 발행이 불가하다.")
    void canIssue() {
        Coupon coupon = new Coupon(0L, 0L);

        assertThatThrownBy(coupon::issue)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("쿠폰 발행 한도에 도달했습니다.");
    }
}
