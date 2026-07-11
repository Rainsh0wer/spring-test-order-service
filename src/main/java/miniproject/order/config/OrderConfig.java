package miniproject.order.config;

import miniproject.order.application.CancelOrderUseCase;
import miniproject.order.application.ConfirmOrderUseCase;
import miniproject.order.application.CreateOrderUseCase;
import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.application.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderService orderService(OrderRepositoryPort orderRepositoryPort,
                                     InventoryServicePort inventoryServicePort,
                                     OrderEventPublisherPort orderEventPublisherPort) {
        return new OrderService(orderRepositoryPort, inventoryServicePort, orderEventPublisherPort);
    }
}
