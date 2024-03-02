package store.mybooks.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.mybooks.gateway.filter.AdminAuthFilter;
import store.mybooks.gateway.filter.UserAuthFilter;

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
                .route("auth", r -> r.path("/auth/**") // 전부 허용 할 것
                        .uri(urlProperties.getAuth()))
                .route("api_user", p -> p.path("/api/member/**") // 유저 권한이 필요 한 경우
                        .filters(f -> f.filter(new UserAuthFilter().apply(new UserAuthFilter.Config())))
                        .uri("lb://RESOURCE-SERVICE")
                )
                .route("api_admin", p -> p.path("/api/admin/**") // 어드민 권한이 필요 한 경우
                        .filters(f->f.filter(new AdminAuthFilter().apply(new AdminAuthFilter.Config())))
                        .uri("lb://RESOURCE-SERVICE")
                )
                .route("api_all", p -> p.path("/api/**") // 권한이 필요 없는 경우
                        .uri("lb://RESOURCE-SERVICE")
                )
                .build();
    }
}
