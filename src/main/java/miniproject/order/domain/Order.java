package miniproject.order.domain;

import miniproject.order.domain.exception.OrderDomainException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final String orderId;
    private OrderStatus status;
    private final List<OrderItem> items;

    public Order(String orderId, OrderStatus status, List<OrderItem> items) {
        this.orderId = orderId;
        this.status = status;
        this.items = new ArrayList<>(items);
    }

    public static Order create(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new OrderDomainException("Order must have at least one item");
        }
        return new Order(UUID.randomUUID().toString(), OrderStatus.PENDING, items);
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderDomainException("Only PENDING orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderDomainException("Order is already CANCELLED");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
