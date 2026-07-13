package miniproject.order.application.service;

import miniproject.order.application.CancelOrderUseCase;
import miniproject.order.application.ConfirmOrderUseCase;
import miniproject.order.application.CreateOrderUseCase;
import miniproject.order.application.GetOrdersUseCase;
import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderItem;
import miniproject.order.domain.exception.OrderDomainException;

import java.util.List;

public class OrderService implements CreateOrderUseCase, ConfirmOrderUseCase, CancelOrderUseCase, GetOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryServicePort inventoryServicePort;
    private final OrderEventPublisherPort orderEventPublisherPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort,
                        InventoryServicePort inventoryServicePort,
                        OrderEventPublisherPort orderEventPublisherPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.inventoryServicePort = inventoryServicePort;
        this.orderEventPublisherPort = orderEventPublisherPort;
    }

    @Override
    public String createOrder(List<OrderItem> items) {
        Order order = Order.create(items);
        order = orderRepositoryPort.save(order); // Saved as PENDING

        try {
            inventoryServicePort.reserveStock(items);
        } catch (Exception e) {
            // If  fails 
            order.cancel();
            orderRepositoryPort.save(order);
            throw new OrderDomainException("Failed to reserve stock. Order cancelled. Reason: " + e.getMessage());
        }

        // If reserve succeeds
        confirmOrder(order.getOrderId());
        return order.getOrderId();
    }

    @Override
    public void confirmOrder(String orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Order not found: " + orderId));

        order.confirm();
        orderRepositoryPort.save(order);
        
        // Publish event after confirm
        orderEventPublisherPort.publishOrderCreatedEvent(orderId);
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Order not found: " + orderId));

        order.cancel();
        orderRepositoryPort.save(order);

        // Release stock
        inventoryServicePort.releaseStock(order.getItems());
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepositoryPort.findAll();
    }
}
