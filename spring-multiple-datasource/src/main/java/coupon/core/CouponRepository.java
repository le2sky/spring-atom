package coupon.core;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("""  
            select new coupon.core.CouponSummary(c.id, c.issueLimit - c.issueCount)
            from Coupon c
            order by c.id asc
            """)
    List<CouponSummary> queryCouponSummaries();
}
