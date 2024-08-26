package batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class JdbcOrderService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createOrder(List<OrderRequest> requests) {
        String sql = "insert into orders (address) values (?)";

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int i
                    ) throws SQLException {
                        OrderRequest orderRequest = requests.get(i);
                        ps.setString(1, orderRequest.address());
                    }

                    @Override
                    public int getBatchSize() {
                        return requests.size();
                    }
                }
        );
    }
}
