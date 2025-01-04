package ma.yassine.ecomorderservice.repositories;

import ma.yassine.ecomorderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
