package miniproject.order.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderJpaEntity {

    @Id
    private String orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    public static OrderJpaEntity fromDomain(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setOrderId(order.getOrderId());
        entity.setStatus(order.getStatus());
        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(item -> OrderItemJpaEntity.fromDomain(item, entity))
                .collect(Collectors.toList());
        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain() {
        return new Order(
                orderId,
                status,
                items.stream().map(OrderItemJpaEntity::toDomain).collect(Collectors.toList())
        );
    }
}
