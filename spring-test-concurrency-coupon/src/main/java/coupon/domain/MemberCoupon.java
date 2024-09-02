package coupon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isUsed;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    public static MemberCoupon issue(Member member, Coupon coupon) {
        coupon.issue();
        return new MemberCoupon(null, false, member, coupon);
    }

    public Benefit exchange() {
        if (isUsed) {
            throw new IllegalStateException("이미 교환된 쿠폰입니다.");
        }

        isUsed = true;
        return new Benefit(null, member, coupon.calculateBenefit());
    }
}
