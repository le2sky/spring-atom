package v2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PreviewProductV2JpaRepository extends JpaRepository<PreviewProduct, Long> {

    @Query("""
            select distinct pp
            from PreviewProduct pp
            join fetch pp.product p
            join fetch p.categories pc
            join fetch pc.category c
            order by pp.id desc
            """)
    List<PreviewProduct> findAllPreviewProductV1();
}
