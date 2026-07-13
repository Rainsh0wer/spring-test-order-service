package miniproject.order.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import miniproject.order.application.exception.InventoryUnavailableException;
import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.domain.OrderItem;
import miniproject.order.infrastructure.client.InventoryFeignClient;
import miniproject.order.infrastructure.client.dto.StockRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InventoryServiceAdapter implements InventoryServicePort {

    private final InventoryFeignClient feignClient;

    public InventoryServiceAdapter(InventoryFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @Override
    public void reserveStock(List<OrderItem> items) {
        List<OrderItem> reservedItems = new ArrayList<>();
        for (OrderItem item : items) {
            try {
                feignClient.reserveStock(new StockRequestDto(item.getProductId(), item.getQuantity()));
                reservedItems.add(item);
            } catch (Exception e) {
                releaseReservedItems(reservedItems);
                throw new InventoryUnavailableException(
                        "Failed to reserve stock for productId: " + item.getProductId(), e);
            }
        }
    }

    @Override
    public void releaseStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            try {
                feignClient.releaseStock(new StockRequestDto(item.getProductId(), item.getQuantity()));
            } catch (Exception e) {
                throw new InventoryUnavailableException(
                        "Failed to release stock for productId: " + item.getProductId(), e);
            }
        }
    }

    private void releaseReservedItems(List<OrderItem> reservedItems) {
        for (OrderItem item : reservedItems) {
            try {
                feignClient.releaseStock(new StockRequestDto(item.getProductId(), item.getQuantity()));
            } catch (Exception e) {
                log.error("Compensation failed: could not release reserved stock for productId: {}",
                        item.getProductId(), e);
            }
        }
    }
}
