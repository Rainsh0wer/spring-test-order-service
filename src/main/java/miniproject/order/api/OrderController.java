package miniproject.order.api;

import miniproject.order.api.dto.ApiResponse;
import miniproject.order.api.dto.CreateOrderRequest;
import miniproject.order.api.dto.OrderDetailResponse;
import miniproject.order.api.dto.OrderResponse;
import miniproject.order.application.command.CancelOrderUseCase;
import miniproject.order.application.command.CreateOrderUseCase;
import miniproject.order.application.query.GetOrdersUseCase;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderItem;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrdersUseCase getOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           GetOrdersUseCase getOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrdersUseCase = getOrdersUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Validated @RequestBody CreateOrderRequest request) {
        List<OrderItem> items = request.getItems().stream()
                .map(item -> new OrderItem(item.getProductId(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());

        String orderId = createOrderUseCase.createOrder(items);

        return ResponseEntity.ok(ApiResponse.success(new OrderResponse(orderId), "Order created successfully"));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String orderId) {
        cancelOrderUseCase.cancelOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getAllOrders() {
        List<Order> orders = getOrdersUseCase.getAllOrders();
        List<OrderDetailResponse> responses = orders.stream()
                .map(OrderDetailResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Orders fetched successfully"));
    }
}
