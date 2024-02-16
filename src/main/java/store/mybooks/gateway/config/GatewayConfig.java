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
     *
     * @param builder .
     * @return route locator
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .uri(urlProperties.getAuth()))
                .route("resource_route", r -> r.path("/api/**")
                        .uri(urlProperties.getResource()))
                .build();
    }
}
