package cartesian;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<ProductCategory> categories = new ArrayList<>();

    public Product(Long id, List<Category> categories) {
        this.id = id;
        this.categories = mapToProductCategory(categories);
    }

    private List<ProductCategory> mapToProductCategory(List<Category> categories) {
        return categories.stream()
                .map(it -> new ProductCategory(null, this, it))
                .toList();
    }
}
