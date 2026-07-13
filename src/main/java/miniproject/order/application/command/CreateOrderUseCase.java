package miniproject.order.application.command;

import miniproject.order.domain.OrderItem;

import java.util.List;

public interface CreateOrderUseCase {
    String createOrder(List<OrderItem> items);
}
