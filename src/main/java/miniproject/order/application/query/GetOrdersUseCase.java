package miniproject.order.application.query;

import miniproject.order.domain.Order;

import java.util.List;

public interface GetOrdersUseCase {
    List<Order> getAllOrders();
}
