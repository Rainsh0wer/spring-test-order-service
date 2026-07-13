package miniproject.order.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private String orderId;
    private OrderStatus status;
    private List<OrderItemResponse> items;

    public static OrderDetailResponse fromDomain(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());
        return new OrderDetailResponse(order.getOrderId(), order.getStatus(), itemResponses);
    }
}
