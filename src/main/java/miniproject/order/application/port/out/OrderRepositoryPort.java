package miniproject.order.application.port.out;

import miniproject.order.domain.Order;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(String orderId);
}
