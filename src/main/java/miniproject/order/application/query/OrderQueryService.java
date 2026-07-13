package miniproject.order.application.query;

import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.domain.Order;

import java.util.List;

public class OrderQueryService implements GetOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderQueryService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepositoryPort.findAll();
    }
}
