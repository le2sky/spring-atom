package batch;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
class JdbcOrderService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createOrder(List<OrderRequest> requests) {
        // order batch insert
        String orderInsertSql = "insert into orders (address) values (?)";
        jdbcTemplate.batchUpdate(orderInsertSql, createOrderPss(requests));

        // batch insert 첫 번째 row pk 조회
        Long firstRowPk = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);

        // orderId 매핑
        for (int i = 0; i < requests.size(); i++) {
            OrderRequest orderRequest = requests.get(i);
            orderRequest.setId(firstRowPk + i);
        }

        // insert할 orderLineItemRequest 생성
        List<OrderLineItemRequest> orderLineItemRequests = requests.stream()
                .flatMap(it -> it.getOrderLineItemRequestsWithPk().stream())
                .toList();

        // item batch insert
        String itemInsertSql = "insert into order_line_item (product_id, quantity, order_id) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(itemInsertSql, createItemPss(orderLineItemRequests));
    }

    private BatchPreparedStatementSetter createOrderPss(List<OrderRequest> requests) {
        return new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderRequest orderRequest = requests.get(i);
                ps.setString(1, orderRequest.getAddress());
            }

            @Override
            public int getBatchSize() {
                return requests.size();
            }
        };
    }

    private BatchPreparedStatementSetter createItemPss(List<OrderLineItemRequest> requests) {
        return new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderLineItemRequest orderLineItemRequest = requests.get(i);
                ps.setLong(1, orderLineItemRequest.getProductId());
                ps.setLong(2, orderLineItemRequest.getQuantity());
                ps.setLong(3, orderLineItemRequest.getOrderId());
            }

            @Override
            public int getBatchSize() {
                return requests.size();
            }
        };
    }
}
