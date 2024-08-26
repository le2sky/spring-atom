package batch;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.Lists;

class JdbcOrderServiceTest extends TestContainerSupport {

    @Autowired
    private JdbcOrderService jdbcOrderService;

    @Test
    @DisplayName("jdbc로 order 데이터 1만개를 저장한다. (orderLineItem 미포함)")
    void createWithOutItem() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequestsWithoutItem();

        jdbcOrderService.createOrder(orderRequests);
    }

    @Test
    @DisplayName("jdbc로 order 데이터 1만개를 분할해서 저장한다. (orderLineItem 미포함)")
    void createWithDivide() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequestsWithoutItem();
        List<List<OrderRequest>> partition = Lists.partition(orderRequests, 1000);

        partition.forEach(jdbcOrderService::createOrder);
    }
}
