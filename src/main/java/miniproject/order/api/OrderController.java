package miniproject.order.api;

import miniproject.order.api.dto.ApiResponse;
import miniproject.order.api.dto.CreateOrderRequest;
import miniproject.order.api.dto.OrderDetailResponse;
import miniproject.order.api.dto.OrderResponse;
import miniproject.order.application.service.OrderService;
import miniproject.order.domain.Order;
import miniproject.order.domain.OrderItem;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Validated @RequestBody CreateOrderRequest request) {
        List<OrderItem> items = request.getItems().stream()
                .map(req -> new OrderItem(req.getProductId(), req.getQuantity(), req.getPrice()))
                .collect(Collectors.toList());

        String orderId = orderService.createOrder(items);

        return ResponseEntity.ok(ApiResponse.success(new OrderResponse(orderId), "Order created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDetailResponse> responses = orders.stream()
                .map(OrderDetailResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Orders fetched successfully"));
    }
}
