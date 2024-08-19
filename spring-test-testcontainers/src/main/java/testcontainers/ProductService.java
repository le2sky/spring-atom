package testcontainers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void addProduct(String name, int quantity) {
        Product product = Product.produce(name, quantity);

        productRepository.save(product);
    }
}
