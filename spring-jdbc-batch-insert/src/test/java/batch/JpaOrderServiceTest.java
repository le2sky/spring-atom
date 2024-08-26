package batch;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.Lists;

/**
 * 삽입 완료 후, 엔티티 식별자가 매핑되어야 한다.
 * 하지만 bulk insert는 모든 엔티티의 식별자를 알 수 없다.
 * 따라서 row별로 write 쿼리가 발생한다.
 */
class JpaOrderServiceTest extends TestContainerSupport {

    @Autowired
    private JpaOrderService jpaOrderService;

    @Test
    @DisplayName("jpa로 order 데이터 1만개를 저장한다. (orderLineItem 미포함)")
    void createWithOutItem() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequestsWithoutItem();

        jpaOrderService.createOrder(orderRequests);
    }

    @Test
    @DisplayName("jpa로 order 데이터 1만개를 저장한다. (orderLineItem 포함)")
    void createWithOutDivide() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequests();

        jpaOrderService.createOrder(orderRequests);
    }

    @Test
    @DisplayName("jpa로 order 데이터 1만개를 분할해서 저장한다. (orderLineItem 포함)")
    void createWithDivide() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequests();
        List<List<OrderRequest>> partition = Lists.partition(orderRequests, 1000);

        partition.forEach(jpaOrderService::createOrder);
    }
}
