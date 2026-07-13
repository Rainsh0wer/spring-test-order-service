package miniproject.order.application.command;

import miniproject.order.application.exception.InventoryUnavailableException;
import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderItem;
import miniproject.order.domain.exception.OrderDomainException;

import java.util.List;

public class OrderCommandService implements CreateOrderUseCase, ConfirmOrderUseCase, CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryServicePort inventoryServicePort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public OrderCommandService(OrderRepositoryPort orderRepositoryPort,
                               InventoryServicePort inventoryServicePort,
                               OrderEventPublisherPort orderEventPublisherPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.inventoryServicePort = inventoryServicePort;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }

    @Override
    public String createOrder(List<OrderItem> items) {
        Order order = Order.create(items);
        orderRepositoryPort.save(order);
        reserveStockOrCancel(order);
        confirmOrder(order.getOrderId());
        return order.getOrderId();
    }

    @Override
    public void confirmOrder(String orderId) {
        Order order = loadOrder(orderId);
        order.confirm();
        orderRepositoryPort.save(order);
        orderEventPublisherPort.publishOrderCreatedEvent(orderId);
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = loadOrder(orderId);
        order.cancel();
        orderRepositoryPort.save(order);
        inventoryServicePort.releaseStock(order.getItems());
    }

    private void reserveStockOrCancel(Order order) {
        try {
            inventoryServicePort.reserveStock(order.getItems());
        } catch (InventoryUnavailableException e) {
            order.cancel();
            orderRepositoryPort.save(order);
            throw new OrderDomainException("Order cancelled because stock could not be reserved: " + e.getMessage());
        }
    }

    private Order loadOrder(String orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Order not found: " + orderId));
    }
}
