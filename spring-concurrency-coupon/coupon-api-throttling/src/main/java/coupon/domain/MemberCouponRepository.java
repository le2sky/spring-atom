package coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    boolean existsMemberCouponByMemberIdAndCouponId(Long memberId, Long couponId);
}
