package store.mybooks.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import store.mybooks.gateway.exception.InvalidPermissionException;
import store.mybooks.gateway.handler.ErrorResponseHandler;
import store.mybooks.gateway.utils.HttpUtils;
import store.mybooks.gateway.validator.TokenValidator;

/**
 * packageName    : store.mybooks.gateway.filter<br>
 * fileName       : UserAuthFilter<br>
 * author         : masiljangajji<br>
 * date           : 2/28/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/28/24        masiljangajji       최초 생성
 */
@Component
@Slf4j
public class UserAuthFilter extends AbstractGatewayFilterFactory<UserAuthFilter.Config> {

    public UserAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 헤더에서 값을 읽어옴

            String token = HttpUtils.getAuthorizationHeaderValue(exchange);
            String originalPath = HttpUtils.getPath(exchange);

            DecodedJWT jwt;

            try {
                jwt = TokenValidator.isValidToken(token);
                TokenValidator.isValidAuthority(jwt, Config.STATUS_ACTIVE, Config.ROLE_USER,Config.ROLE_ADMIN);

            } catch (JWTVerificationException e) { // 토큰 검증실패
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED); // 토큰이 이상함 인증이 필요
            } catch (InvalidPermissionException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN); //  토큰은 유효한데 권한 없음
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .path(originalPath.replace("/api/member/", "/api/")) // 새로운 URL 경로 설정
                    .header("X-User-Id", jwt.getSubject()) // 유저 정보 보내기
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);
        };
    }

    public static class Config { // // 필요한 전달할 설정
        private static final String ROLE_USER = "ROLE_USER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
        private static final String STATUS_ACTIVE = "활성";
    }
}

