package v1;

import java.util.List;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PreviewProductV1JpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PreviewProductV1JpaRepository previewProductV1JpaRepository;

    @Test
    @DisplayName("중복 카테고리를 조회한다.")
    void categoryDuplication() {
        initData();
        // 이 경우 n + 1은 발생하지 않고, 중복 카테고리가 생기지 않는다.
        List<v1.PreviewProduct> result = previewProductV1JpaRepository.findAllPreviewProduct();
    }

    private void initData() {
        v1.Category a = new v1.Category(null, "A");
        v1.Category b = new v1.Category(null, "B");
        v1.Category c = new v1.Category(null, "C");
        v1.Category d = new v1.Category(null, "D");
        v1.Category e = new v1.Category(null, "E");
        em.persist(a);
        em.persist(b);
        em.persist(c);
        em.persist(d);
        em.persist(e);

        v1.Product product1 = new v1.Product(null, List.of(a, b, c, d, e));
        v1.Product product2 = new v1.Product(null, List.of(a, b, c, d, e));
        em.persist(product1);
        em.persist(product2);

        v1.PreviewProduct previewProduct1 = new v1.PreviewProduct(null, product1);
        v1.PreviewProduct previewProduct2 = new v1.PreviewProduct(null, product1);
        v1.PreviewProduct previewProduct3 = new v1.PreviewProduct(null, product2);
        v1.PreviewProduct previewProduct4 = new v1.PreviewProduct(null, product2);
        em.persist(previewProduct1);
        em.persist(previewProduct2);
        em.persist(previewProduct3);
        em.persist(previewProduct4);

        em.flush();
        em.clear();
    }
}
