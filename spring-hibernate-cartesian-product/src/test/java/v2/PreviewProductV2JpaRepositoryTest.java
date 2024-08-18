package v2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("v2")
@Sql(value = "classpath:oom.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PreviewProductV2JpaRepositoryTest {

    @Autowired
    private PreviewProductV2JpaRepository previewProductV2JpaRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("v1 쿼리는 전체 데이터를 로드하면 메모리 부족이 발생한다.")
    void v1() {
        initData();
        // product의 카테고리가 20개이며, 전체 preview_category 데이터가 20만건인 경우 = 600만건이 로드된다.
        List<PreviewProduct> result = previewProductV2JpaRepository.findAllPreviewProductV1();
    }

    private void initData() {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            for (int j = 0; j < 200_000; j++) {
                System.out.println(j + "번째 preview product insert");
                String sql = "insert into preview_product_v2 (product_id) values (1)";
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
