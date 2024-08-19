package testcontainers;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
@Transactional
@ActiveProfiles("mysql")
class ProductServiceV1Test {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private MySQLContainer mySQLContainer = new MySQLContainer("mysql:8");

    @BeforeEach
    void setUp() {
        mySQLContainer.start();
    }

    @AfterEach
    void tearDown() {
        mySQLContainer.stop();
    }

    @Test
    @DisplayName("신규 제품을 생성한다.")
    void produce() {
        productService.addProduct("맥북 프로", 10);

        List<Product> all = productRepository.findAll();

        Assertions.assertEquals(1, all.size());
    }
}
