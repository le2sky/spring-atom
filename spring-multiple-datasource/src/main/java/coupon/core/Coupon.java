package coupon.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "issue_count")
    private Long issueCount;

    @Column(nullable = false, name = "issue_limit")
    private Long issueLimit;

    public Coupon(Long issueCount, Long issueLimit) {
        this(null, issueCount, issueLimit);
    }

    public void issue() {
        if (issueCount >= issueLimit) {
            throw new IllegalStateException("쿠폰 발행 한도에 도달했습니다.");
        }

        issueCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coupon coupon)) {
            return false;
        }

        return this.getId() != null && Objects.equals(getId(), coupon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
