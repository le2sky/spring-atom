package v2;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    @Query("""
            select distinct pp
            from PreviewProduct pp
            join fetch pp.product p
            join fetch p.categories pc
            join fetch pc.category c
            order by pp.id desc
            """)
    Slice<PreviewProduct> findAllPreviewProductV2(Pageable pageable);
}
