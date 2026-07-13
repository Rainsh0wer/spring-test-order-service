package miniproject.order.infrastructure.adapter;

import miniproject.order.application.port.out.OrderRepositoryPort;
import miniproject.order.domain.Order;
import miniproject.order.infrastructure.entity.OrderJpaEntity;
import miniproject.order.infrastructure.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.fromDomain(order);
        OrderJpaEntity saved = orderJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return orderJpaRepository.findById(orderId)
                .map(OrderJpaEntity::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
