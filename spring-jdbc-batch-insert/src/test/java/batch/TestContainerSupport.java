package batch;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@ActiveProfiles("batch")
abstract class TestContainerSupport {

    protected static MySQLContainer container = new MySQLContainer("mysql:8.0");

    static {
        container.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String url = container.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC&rewriteBatchedStatements=true&profileSQL=true&logger=Slf4JLogger&maxQuerySizeToLog=1000";
        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "test");
    }
}
