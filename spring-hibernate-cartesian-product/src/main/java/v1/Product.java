package v1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_v1")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OrderBy(value = "id ASC")
    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private Set<ProductCategory> categories = new HashSet<>();

    public Product(Long id, List<Category> categories) {
        this.id = id;
        this.categories = new HashSet<>(mapToProductCategory(categories));
    }

    private List<ProductCategory> mapToProductCategory(List<Category> categories) {
        return categories.stream()
                .map(it -> new ProductCategory(null, this, it))
                .toList();
    }
}
