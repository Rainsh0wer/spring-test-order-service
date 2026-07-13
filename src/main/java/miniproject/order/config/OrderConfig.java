package miniproject.order.config;

import miniproject.order.application.command.OrderCommandService;
import miniproject.order.application.port.out.InventoryServicePort;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.application.query.OrderQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderCommandService orderCommandService(OrderRepositoryPort orderRepositoryPort,
                                                   InventoryServicePort inventoryServicePort,
                                                   OrderEventPublisherPort orderEventPublisherPort) {
        return new OrderCommandService(orderRepositoryPort, inventoryServicePort, orderEventPublisherPort);
    }

    @Bean
    public OrderQueryService orderQueryService(OrderRepositoryPort orderRepositoryPort) {
        return new OrderQueryService(orderRepositoryPort);
    }
}
