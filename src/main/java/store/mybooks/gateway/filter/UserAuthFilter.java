package store.mybooks.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import store.mybooks.gateway.error.ErrorMessage;
import store.mybooks.gateway.exception.ForbiddenAccessException;
import store.mybooks.gateway.exception.InvalidStatusException;
import store.mybooks.gateway.exception.StatusIsDormancyException;
import store.mybooks.gateway.exception.StatusIsLockException;
import store.mybooks.gateway.handler.ErrorResponseHandler;
import store.mybooks.gateway.redis.RedisService;
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
@Slf4j
public class UserAuthFilter extends AbstractGatewayFilterFactory<UserAuthFilter.Config> {

    private final RedisService redisService;

    public UserAuthFilter(RedisService redisService) {
        super(Config.class);
        this.redisService = redisService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String token = HttpUtils.getAuthorizationHeaderValue(exchange);
            String originalPath = HttpUtils.getPath(exchange);

            DecodedJWT jwt;

            try {
                jwt = TokenValidator.isValidToken(token);
                String status = jwt.getClaim("status").asString();

                if (Arrays.stream(Config.EXCLUDE_STATUS_URL)
                        .noneMatch(originalPath::contains)) {
                    TokenValidator.isValidStatus(status);
                }

                TokenValidator.isValidAuthority(jwt.getClaim("authority").asString(), Config.ROLE_USER,
                        Config.ROLE_ADMIN);

            } catch (StatusIsDormancyException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.STATUS_IS_DORMANT_EXCEPTION.getMessage()); //  토큰은 유효한데 휴면 상태임
            } catch (StatusIsLockException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.STATUS_IS_LOCK_EXCEPTION.getMessage()); //  토큰은 유효한데 잠금 상태임
            } catch (ForbiddenAccessException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.INVALID_ACCESS.getMessage()); //  토큰은 유효한데 권한 없음 403
            } catch (TokenExpiredException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.TOKEN_EXPIRED.getMessage()); // 토큰 만료됐음 인증 필요 401
            } catch (JWTVerificationException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.INVALID_TOKEN.getMessage()); // 토큰이 조작됐음 올바르지 않은 요청 401
            }

            String key = jwt.getSubject() + exchange.getRequest().getRemoteAddress();

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .path(originalPath.replace("/api/member/", "/api/")) // 새로운 URL 경로 설정
                    .header("X-User-Id", redisService.getValues(
                            key.replaceFirst("/", "").split(":")[0])) // 유저 정보 보내기
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

        private static final String[] EXCLUDE_STATUS_URL = {"/dormancy", "/lock"};
    }
}

