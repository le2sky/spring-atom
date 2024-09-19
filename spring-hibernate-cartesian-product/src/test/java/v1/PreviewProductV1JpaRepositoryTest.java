package v1;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class PreviewProductV1JpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PreviewProductV1JpaRepository previewProductV1JpaRepository;

    @Test
    @DisplayName("중복되지 않은 카테고리를 조회한다.")
    void categoryDuplication() {
        initData();
        // 이 경우 n + 1은 발생하지 않고, 중복 카테고리가 생기지 않는다.
        List<PreviewProduct> result = previewProductV1JpaRepository.findAllPreviewProduct();
    }

    private void initData() {
        Category a = new Category(null, "A");
        Category b = new Category(null, "B");
        Category c = new Category(null, "C");
        Category d = new Category(null, "D");
        Category e = new Category(null, "E");
        em.persist(a);
        em.persist(b);
        em.persist(c);
        em.persist(d);
        em.persist(e);

        Product product1 = new Product(null, List.of(a, b, c, d, e));
        Product product2 = new Product(null, List.of(a, b, c, d, e));
        em.persist(product1);
        em.persist(product2);

        PreviewProduct previewProduct1 = new PreviewProduct(null, product1);
        PreviewProduct previewProduct2 = new PreviewProduct(null, product1);
        PreviewProduct previewProduct3 = new PreviewProduct(null, product2);
        PreviewProduct previewProduct4 = new PreviewProduct(null, product2);
        em.persist(previewProduct1);
        em.persist(previewProduct2);
        em.persist(previewProduct3);
        em.persist(previewProduct4);

        em.flush();
        em.clear();
    }
}
