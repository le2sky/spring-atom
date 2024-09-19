package coupon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCouponTest {

    @Test
    @DisplayName("사용자 쿠폰을 생성한다.")
    void issue() {
        Coupon coupon = createCoupon(1L);
        Member member = createMember();

        assertThatCode(() -> MemberCoupon.issue(member, coupon))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("발행 제한된 쿠폰으로 사용자 쿠폰을 생성한다.")
    void cantIssue() {
        Coupon coupon = createCoupon(0L);
        Member member = createMember();

        assertThatThrownBy(() -> MemberCoupon.issue(member, coupon))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용자 쿠폰을 금액으로 변경한다.")
    void exchange() {
        Coupon coupon = createCoupon(1L);
        Member member = createMember();
        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);

        Benefit benefit = memberCoupon.exchange();

        assertThat(benefit.getMoney()).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("이미 교환된 사용자 쿠폰은 다시 교환이 불가능하다.")
    void alreadyExchanged() {
        Coupon coupon = createCoupon(1L);
        Member member = createMember();
        MemberCoupon memberCoupon = MemberCoupon.issue(member, coupon);
        memberCoupon.exchange();

        assertThatThrownBy(memberCoupon::exchange)
                .isInstanceOf(IllegalStateException.class);
    }

    private Member createMember() {
        return new Member(null, "atom");
    }

    private Coupon createCoupon(Long issueLimit) {
        return new Coupon(
                null,
                "coupon",
                issueLimit,
                0L,
                1000.0,
                new CouponFullAmountStrategy(),
                LocalDateTime.now()
        );
    }
}
