package store.mybooks.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import java.util.Objects;
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
            String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");

            log.warn(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()+"아이피");
            log.warn(userAgent+"유저에이전트");
            log.warn(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For")+"헤더");

            try {
                jwt = TokenValidator.isValidToken(token);

                String key = jwt.getSubject() +
                        Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress() +
                        userAgent;

                // 레디스에 유저 아이디 담은 정보가 없다면 , 이미 로그아웃 한 것 따라서 유효하지 않은 토큰으로 보겠음
                if (Objects.isNull(redisService.getValues(key))) {
                    log.warn("레디스에 유저 아이디 담은 정보가 없음");

                    throw new JWTVerificationException("Logout Token");
                }


                if (Arrays.stream(Config.EXCLUDE_STATUS_URL)
                        .noneMatch(originalPath::contains)) {
                    TokenValidator.isValidStatus(jwt.getClaim("status").asString());
                }

                TokenValidator.isValidAuthority(jwt.getClaim("authority").asString(), Config.ROLE_USER,
                        Config.ROLE_ADMIN);

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .path(originalPath.replace("/api/member/", "/api/")) // 새로운 URL 경로 설정
                        .header("X-User-Id", redisService.getValues(key)) // 유저 정보 보내기
                        .build();

                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(modifiedRequest)
                        .build();

                return chain.filter(modifiedExchange);

            } catch (StatusIsDormancyException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.STATUS_IS_DORMANT_EXCEPTION.getMessage()); //  토큰은 유효한데 휴면 상태임
            } catch (StatusIsLockException e) {
                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.STATUS_IS_LOCK_EXCEPTION.getMessage()); //  토큰은 유효한데 잠금 상태임
            } catch (ForbiddenAccessException e) {
                log.warn("권한없음");

                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.FORBIDDEN,
                        ErrorMessage.INVALID_ACCESS.getMessage()); //  토큰은 유효한데 권한 없음 403
            } catch (TokenExpiredException e) {
                log.warn("만료");

                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.TOKEN_EXPIRED.getMessage()); // 토큰 만료됐음 인증 필요 401
            } catch (JWTVerificationException e) {
                log.warn("조작");

                return ErrorResponseHandler.handleInvalidToken(exchange, HttpStatus.UNAUTHORIZED,
                        ErrorMessage.INVALID_TOKEN.getMessage()); // 토큰이 조작됐음 올바르지 않은 요청 401
            }
        };
    }

    public static class Config { // // 필요한 전달할 설정
        private static final String ROLE_USER = "ROLE_USER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";

        private static final String[] EXCLUDE_STATUS_URL = {"/dormancy", "/lock"};
    }
}

