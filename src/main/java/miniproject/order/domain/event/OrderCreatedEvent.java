package miniproject.order.domain.event;

import java.time.Instant;
import java.util.UUID;

public class OrderCreatedEvent {
    private final String eventId;
    private final Instant occurredAt;
    private final String orderId;

    public OrderCreatedEvent(String orderId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.orderId = orderId;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getOrderId() {
        return orderId;
    }
}
