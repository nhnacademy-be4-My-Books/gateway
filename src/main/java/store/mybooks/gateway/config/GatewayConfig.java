package store.mybooks.gateway.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : store.mybooks.gateway.config
 * fileName       : GatewayConfig
 * author         : damho
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24          damho-lee          최초 생성
 */
@Configuration
public class GatewayConfig {

    @Value("${auth.url.name}")
    private String authUrl;

    @Value("${resource.url.name}")
    private String resourceUrl;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .uri(authUrl))
                .route("resource_route", r -> r.path("/api/**")
                        .uri(resourceUrl))
                .build();
    }
}
