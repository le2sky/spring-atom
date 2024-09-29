package coupon.domain;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    boolean existsMemberCouponByMemberIdAndCouponId(Long memberId, Long couponId);
}
