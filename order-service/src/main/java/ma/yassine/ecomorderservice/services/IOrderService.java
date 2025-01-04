package ma.yassine.ecomorderservice.services;

import ma.yassine.ecomorderservice.entities.Order;
import ma.yassine.ecomorderservice.entities.ProductItem;
import ma.yassine.ecomorderservice.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrderService {
    ResponseEntity<List<Order>> getOrders();

    ResponseEntity<Order> getOrder(Long id);

    ResponseEntity<Order> createNewOrder(List<Product> products);

    void deleteOrder(Long id);

    ResponseEntity<String> confirmOrder(Long orderId);

    ResponseEntity<String> cancelOrder(Long orderId);

    ResponseEntity<String> deliverOrder(Long orderId);

    void printProducts();

    List<ProductItem> getProductItems();
}
