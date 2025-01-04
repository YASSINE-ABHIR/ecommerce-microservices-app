package ma.yassine.ecomorderservice.repositories;

import ma.yassine.ecomorderservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemsRepository extends JpaRepository<ProductItem, Long> {
}
