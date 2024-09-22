package testcontainers;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ProductServiceV1Test extends TestContainerSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("신규 제품을 생성한다.")
    void produce() {
        productService.addProduct("맥북 프로", 10);

        List<Product> all = productRepository.findAll();

        Assertions.assertEquals(1, all.size());
    }
}
