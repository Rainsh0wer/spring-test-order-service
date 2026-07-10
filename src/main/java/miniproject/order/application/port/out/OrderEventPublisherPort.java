package miniproject.order.application.port.out;

public interface OrderEventPublisherPort {
    void publishOrderCreatedEvent(String orderId);
}
