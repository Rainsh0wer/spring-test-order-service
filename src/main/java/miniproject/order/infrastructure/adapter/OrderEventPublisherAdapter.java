package miniproject.order.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

    @Override
    public void publishOrderCreatedEvent(String orderId) {
        log.info("Dummy publish event for orderId: {}", orderId);
        // This will be implemented with RabbitMQ in Task 6
    }
}
