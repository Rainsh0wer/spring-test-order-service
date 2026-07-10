package miniproject.order.application.port.out;

import miniproject.order.domain.OrderItem;
import java.util.List;

public interface InventoryServicePort {
    void reserveStock(List<OrderItem> items);
    void releaseStock(List<OrderItem> items);
}
