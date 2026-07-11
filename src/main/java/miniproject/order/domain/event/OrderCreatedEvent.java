package miniproject.order.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class OrderCreatedEvent {
    private String eventId;
    private Instant occurredAt;
    private String orderId;

    public OrderCreatedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
    }

    public OrderCreatedEvent(String orderId) {
        this();
        this.orderId = orderId;
    }
}
