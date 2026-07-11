package miniproject.order.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import miniproject.order.application.port.out.OrderEventPublisherPort;
import miniproject.order.config.RabbitMqConfig;
import miniproject.order.domain.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventPublisherAdapter implements OrderEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderCreatedEvent(String orderId) {
        OrderCreatedEvent event = new OrderCreatedEvent(orderId);
        log.info("Publishing OrderCreatedEvent for orderId: {}", orderId);
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_ORDER_CREATED, event);
    }
}
