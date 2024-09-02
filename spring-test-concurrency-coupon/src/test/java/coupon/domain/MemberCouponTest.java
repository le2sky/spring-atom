package coupon.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberCouponTest {

    @Test
    @DisplayName("사용자 쿠폰을 생성한다.")
    void issue() {
        Coupon coupon = createCoupon(1L);

        assertThatCode(() -> MemberCoupon.issue(coupon))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("발행 제한된 쿠폰으로 사용자 쿠폰을 생성한다.")
    void cantIssue() {
        Coupon coupon = createCoupon(0L);

        assertThatThrownBy(() -> MemberCoupon.issue(coupon))
                .isInstanceOf(IllegalStateException.class);
    }

    private Coupon createCoupon(Long issueLimit) {
        return new Coupon(null, "coupon", issueLimit, 0L, 1000.0, null, LocalDateTime.now());
    }
}
