package v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreviewProductV1JpaRepository extends JpaRepository<PreviewProduct, Long> {

    @Query("""
            select distinct pp
            from PreviewProduct pp
            join fetch pp.product p
            join fetch p.categories pc
            join fetch pc.category c
            order by pp.id desc
            """)
    List<PreviewProduct> findAllPreviewProduct();
}
