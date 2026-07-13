package miniproject.order.infrastructure.adapter;

import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.domain.OrderItem;
import miniproject.order.domain.exception.OrderDomainException;
import miniproject.order.infrastructure.client.InventoryFeignClient;
import miniproject.order.infrastructure.client.dto.StockRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryServiceAdapter implements InventoryServicePort {

    private final InventoryFeignClient feignClient;

    public InventoryServiceAdapter(InventoryFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public void reserveStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            try {
                StockRequestDto request = new StockRequestDto(item.getProductId(), item.getQuantity());
                feignClient.reserveStock(request);
            } catch (Exception e) {
                throw new OrderDomainException("Failed to reserve stock for productId: " + item.getProductId() + ". Reason: " + e.getMessage());
            }
        }
    }

    @Override
    public void releaseStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            try {
                StockRequestDto request = new StockRequestDto(item.getProductId(), item.getQuantity());
                feignClient.releaseStock(request);
            } catch (Exception e) {
                throw new OrderDomainException("Failed to release stock for productId: " + item.getProductId() + ". Reason: " + e.getMessage());
            }
        }
    }
}
