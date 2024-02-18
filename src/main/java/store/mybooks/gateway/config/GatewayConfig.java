package store.mybooks.gateway.config;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GatewayConfig {
    private final UrlProperties urlProperties;

    /**
     * methodName : customRouteLocator
     * author : damho-lee
     * description : gateway 설정. 각각 해당하는 서버로 요청 보내준다.
     * auth로 들어온 요청은 auth서버로 처리한다.
     * resource 서버는 Eureka를 이용해 라운드 로빈 방식으로 동작한다.
     *
     * @param builder .
     * @return route locator
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .uri(urlProperties.getAuth()))
                .route("resource-service", p -> p.path("/api/**").and()
                        .uri("lb://RESOURCE-SERVICE")
                )
                .build();
    }
}
