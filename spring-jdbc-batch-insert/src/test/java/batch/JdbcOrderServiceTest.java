package batch;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.Lists;

/**
 * 외부에서 주어지는 order, orderLineItem 1:N 정보를
 * 각 테이블에 마이그레이션 insert하는 경우 batch insert가 구조적으로 불가능하다.
 * insert된 row들의 PK를 알 수 없기 때문에 orderLineItem쪽 외래키를 설정할 수 없기 때문이다.
 * insert 완료 후에 last_insert_id()를 이용하면 insert된 첫 번째 row의 PK를 알 수 있다.
 * 이를 활용하여 N 데이터의 외래키를 삽입할 수 있다. (PK 증가는 항상 1이라는 특징을 이용)
 *
 * @see JdbcOrderService
 */
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
    @DisplayName("jdbc로 order 데이터 1만개를 저장한다. (orderLineItem 포함)")
    void createWithOutDivide() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequests();

        jdbcOrderService.createOrder(orderRequests);
    }

    @Test
    @DisplayName("jdbc로 order 데이터 1만개를 분할해서 저장한다. (orderLineItem 포함)")
    void createWithDivide() {
        List<OrderRequest> orderRequests = OrderRequestGenerator.generateRequests();
        List<List<OrderRequest>> partition = Lists.partition(orderRequests, 1000);

        partition.forEach(jdbcOrderService::createOrder);
    }
}
