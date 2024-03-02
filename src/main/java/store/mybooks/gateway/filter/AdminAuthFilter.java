package store.mybooks.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import store.mybooks.gateway.exception.InvalidPermissionException;
import store.mybooks.gateway.handler.ErrorResponseHandler;
import store.mybooks.gateway.utils.HttpUtils;
import store.mybooks.gateway.validator.TokenValidator;

/**
 * packageName    : store.mybooks.gateway.filter<br>
 * fileName       : AdminAuthFilter<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
public class AdminAuthFilter extends AbstractGatewayFilterFactory<AdminAuthFilter.Config> {

    public AdminAuthFilter() {
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
                TokenValidator.isValidAuthority(jwt, AdminAuthFilter.Config.STATUS_ACTIVE,
                        AdminAuthFilter.Config.ROLE_ADMIN);

            } catch (JWTVerificationException e) { // 토큰 검증실패
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED); // 토큰이 이상함 인증이 필요
            } catch (InvalidPermissionException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN); //  토큰은 유효한데 권한 없음
            }

            exchange.getRequest().mutate()
                    .path(originalPath.replace("/api/admin/", "/api/")) // 새로운 URL 경로 설정
                    .build();

            return chain.filter(exchange);
        };
    }


    public static class Config { // // 필요한 전달할 설정
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
        private static final String STATUS_ACTIVE = "활성";
    }
}
