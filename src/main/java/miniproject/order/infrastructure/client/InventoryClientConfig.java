package miniproject.order.infrastructure.client;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class InventoryClientConfig {

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                5000, TimeUnit.MILLISECONDS,
                10000, TimeUnit.MILLISECONDS,
                true
        );
    }
}
