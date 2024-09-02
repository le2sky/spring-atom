package coupon.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long issueLimit;

    @Column(nullable = false)
    private Long issueCount;

    @Column(nullable = false)
    private Double money;

    @Column(nullable = false)
    @Convert(converter = CouponBenefitStrategyConverter.class, attributeName = "benefit_type")
    private CouponBenefitStrategy benefitStrategy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    public void issue() {
        if (issueLimit <= issueCount) {
            throw new IllegalStateException("발행 한도에 도달했습니다.");
        }

        issueCount++;
    }

    public Double calculateBenefit() {
        return benefitStrategy.calculateBenefit(this);
    }
}
