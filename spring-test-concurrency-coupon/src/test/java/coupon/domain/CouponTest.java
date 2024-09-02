package coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    @DisplayName("쿠폰을 발행한다.")
    void issue() {
        Coupon coupon = createCoupon(1L);

        coupon.issue();

        assertThat(coupon.getIssueCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("발행 제한을 넘기면 더 이상 쿠폰을 발행할 수 없다.")
    void cantIssue() {
        Coupon coupon = createCoupon(0L);

        assertThatThrownBy(coupon::issue)
                .isInstanceOf(IllegalStateException.class);
    }

    private Coupon createCoupon(Long issueLimit) {
        return new Coupon(null, "coupon", issueLimit, 0L, 1000.0, null, LocalDateTime.now());
    }
}
