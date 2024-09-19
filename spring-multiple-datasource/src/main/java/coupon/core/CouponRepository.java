package coupon.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("""  
            select new coupon.core.CouponSummary(c.id, c.issueCount, c.issueLimit)
            from Coupon c
            order by c.id asc
            """)
    List<CouponSummary> queryCouponSummaries();
}
