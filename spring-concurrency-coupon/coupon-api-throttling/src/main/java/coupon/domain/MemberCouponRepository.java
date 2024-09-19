package coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("""
            select ms
            from MemberCoupon ms
            where
                ms.member.id = :memberId and
                ms.coupon.id = :couponId
            """)
    Optional<MemberCoupon> findMemberCoupon(Long memberId, Long couponId);
}
