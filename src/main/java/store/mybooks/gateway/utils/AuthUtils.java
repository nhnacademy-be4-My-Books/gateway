package store.mybooks.gateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import store.mybooks.gateway.redis.RedisService;

/**
 * packageName    : store.mybooks.gateway.utils<br>
 * fileName       : AuthUtils<br>
 * author         : masiljangajji<br>
 * date           : 3/14/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/14/24        masiljangajji       최초 생성
 */
public class AuthUtils {

    private AuthUtils(){}

    public static ServerHttpRequest getAdminRequest(ServerWebExchange exchange, String originalPath) {

        return exchange.getRequest().mutate()
                .path(originalPath.replace("/api/admin/", "/api/")) // 새로운 URL 경로 설정
                .build();
    }

    public static ServerHttpRequest getUserRequest(ServerWebExchange exchange, String originalPath, String key,
                                                RedisService redisService) {

        return exchange.getRequest().mutate()
                .path(originalPath.replace("/api/member/", "/api/")) // 새로운 URL 경로 설정
                .header("X-User-Id", redisService.getValues(key)) // 유저 정보 보내기
                .build();
    }

}
