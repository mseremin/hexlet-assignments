package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findAllByPriceBetweenOrderByPrice(int minPrice, int maxPrice);

    List<Product> findAllByPriceGreaterThanEqualOrderByPrice(int minPrice);

    List<Product> findAllByPriceLessThanEqualOrderByPrice(int maxPrice);
    // END
}
