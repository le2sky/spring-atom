package batch;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT DISTINCT o
            FROM Order o
            JOIN FETCH o.orderLines
            ORDER BY o.id ASC
            """)
    List<Order> findAllWithOrderLines();
}
