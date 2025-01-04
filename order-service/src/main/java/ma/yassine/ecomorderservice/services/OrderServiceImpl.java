package ma.yassine.ecomorderservice.services;

import lombok.AllArgsConstructor;
import ma.yassine.ecomorderservice.entities.Order;
import ma.yassine.ecomorderservice.entities.ProductItem;
import ma.yassine.ecomorderservice.enums.OrderState;
import ma.yassine.ecomorderservice.feignClients.InventoryClient;
import ma.yassine.ecomorderservice.models.Product;
import ma.yassine.ecomorderservice.repositories.OrderRepository;
import ma.yassine.ecomorderservice.repositories.ProductItemsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductItemsRepository productItemsRepository;
    private final InventoryClient inventoryClient;

    /**
     * Retrieves all orders from the database and returns them as a response entity.
     *
     * @return a ResponseEntity containing a list of all orders.
     */
    @Override
    public ResponseEntity<List<Order>> getOrders(){
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves an order based on the provided order ID.
     *
     * @param id the ID of the order to be retrieved
     * @return a ResponseEntity containing the order if found, or a 404 Not Found status if no order exists with the provided ID
     */
    @Override
    public ResponseEntity<Order> getOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null){
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new order by saving it to the database, deducting product quantities from inventory,
     * and persisting individual product items associated with the order.
     *
     * @param products A list of {@code Product} objects representing the products to be included in the order.
     *                 Each product contains information such as its ID, price, and quantity.
     * @return A {@code ResponseEntity<Order>} containing the created {@code Order} object if successful.
     */
    @Override
    public ResponseEntity<Order> createNewOrder(List<Product> products){

        Order savedOrder = orderRepository.save(new Order());
        List<ProductItem> productItems = new ArrayList<>();
        products.parallelStream().forEach(product -> {
            Integer productQuantity = inventoryClient.decrementProductQuantity(product.getId(), product.getQuantity());
            if (productQuantity != null) {
                ProductItem productItem = ProductItem.builder()
                        .order(savedOrder)
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .productId(product.getId())
                        .build();
                productItems.add(productItem);
            } else {
                System.out.println("Error: Product with id '" + product.getId() + "' does not exist or has insufficient quantity.");
                throw new RuntimeException("Error: Product with id '" + product.getId() + "' does not exist or has insufficient quantity.");
            }
        });
        productItemsRepository.saveAll(productItems);
        return ResponseEntity.ok(savedOrder);
    }

    /**
     * Deletes an order with the specified ID from the database.
     *
     * @param id the unique identifier of the order to be deleted
     */
    @Override
    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    /**
     * Confirms an order by updating its state to PROCESSING if it is found and currently in the NEW state.
     *
     * @param orderId The unique identifier of the order to confirm.
     * @return A {@code ResponseEntity<String>} containing:
     *         - A success message with HTTP 200 status if the order is successfully confirmed.
     *         - An error message with HTTP 400 status if the order is not found or is not in the NEW state.
     */
    @Override
    public ResponseEntity<String> confirmOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null && order.getOrderState() == OrderState.NEW){
            order.setOrderState(OrderState.PROCESSING);
            orderRepository.save(order);
            return ResponseEntity.ok("Order confirmed.");
        } else {
            return ResponseEntity.badRequest().body("Order with id '" + orderId + "' was not found or is not in NEW state.");
        }
    }

    /**
     * Cancels the order with the given order ID if it exists and is in a valid state for cancellation.
     * An order can only be cancelled if it is not already in the DELIVERED or CANCELLED state.
     *
     * @param orderId the unique identifier of the order to be cancelled
     * @return a ResponseEntity containing a success message if the order was cancelled,
     *         or a bad request message if the order does not exist or cannot be cancelled
     */
    @Override
    public ResponseEntity<String> cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null && order.getOrderState() != OrderState.DELIVERED && order.getOrderState() != OrderState.CANCELLED){
            order.setOrderState(OrderState.CANCELLED);
            orderRepository.save(order);
            return ResponseEntity.ok("Order cancelled.");
        } else {
            return ResponseEntity.badRequest().body("Order with id '" + orderId + "' was not found or is already in DELIVERED or CANCELLED state.");
        }
    }

    /**
     * Handles the delivery of an order by updating its state to DELIVERED, if applicable.
     *
     * @param orderId the ID of the order to be delivered
     * @return ResponseEntity with a success message if the order was successfully delivered,
     *         or an error message if the order is not found, is canceled, or has already been delivered
     */
    @Override
    public ResponseEntity<String> deliverOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getOrderState() == OrderState.DELIVERED){
            return ResponseEntity.badRequest().body("Order with id '" + orderId + "' was already delivered.");
        } else if (order != null && order.getOrderState() == OrderState.CANCELLED){
            return ResponseEntity.badRequest().body("Order with id '" + orderId + "' was cancelled.");
        } else if (order != null && order.getOrderState() == OrderState.PROCESSING) {
            order.setOrderState(OrderState.DELIVERED);
            orderRepository.save(order);
            return ResponseEntity.ok("Order delivered.");
        }
        return ResponseEntity.badRequest().body("Order with id '" + orderId + "' was not found or is not in PROCESSING state.");
    }

    @Override
    public void printProducts(){
        ResponseEntity<List<Product>> allProducts = inventoryClient.getAllProducts();
        if (allProducts.getStatusCode().is2xxSuccessful() && allProducts.getBody() != null){
            List<Product> products = allProducts.getBody();
            products.forEach(System.out::println);
        } else {
            System.out.println("Error: " + allProducts.getStatusCode());
        }
    }

    @Override
    public List<ProductItem> getProductItems(){
        return productItemsRepository.findAll();
    }
}
