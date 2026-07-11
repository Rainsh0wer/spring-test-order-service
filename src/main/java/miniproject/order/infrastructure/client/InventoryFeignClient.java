package miniproject.order.infrastructure.client;

import miniproject.order.infrastructure.client.dto.ApiResponseDto;
import miniproject.order.infrastructure.client.dto.StockRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}", configuration = InventoryClientConfig.class)
public interface InventoryFeignClient {

    @PostMapping("/api/inventory/reserve")
    ApiResponseDto<Void> reserveStock(@RequestBody StockRequestDto request);

    @PostMapping("/api/inventory/release")
    ApiResponseDto<Void> releaseStock(@RequestBody StockRequestDto request);
}
