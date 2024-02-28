package store.mybooks.gateway.filter;

import static javax.crypto.Cipher.SECRET_KEY;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

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
public class UserAuthFilter extends AbstractGatewayFilterFactory<UserAuthFilter.Config> {

    public UserAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 헤더에서 값을 읽어옴

            ServerHttpRequest request = exchange.getRequest();

            String token = request.getHeaders().getFirst("token");

            String path = exchange.getRequest().getURI().getPath();

            if (!path.equals("/api/users/login")) {
                // 헤더에 token 이 없거나 값이 유효하지 않으면 403 에러 반환
                if (token == null || !isValidToken(token)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete(); // 여기서 return을 추가하여 더 이상의 필터 체인을 진행하지 않도록 설정
                }
            }

            return chain.filter(exchange);
        };
    }


    private boolean isValidToken(String token) {

        try {

            // 알고리즘과 비밀 키로 JWTVerifier 생성
            Algorithm algorithm = Algorithm.HMAC512("이승재"); // todo 키 메니저 달아서 암호화
            JWTVerifier verifier = JWT.require(algorithm).build();

            System.out.println("?@?@?@??@!?#!?@#?!#?!@?#?");

            // 일치하는지 확인
            DecodedJWT jwt = verifier.verify(token);

            System.out.println(jwt.getClaim("authorization").asString());
            System.out.println(jwt.getClaim("this").asInt());
            System.out.println(jwt.getClaim("status").asString());

            String authorization = jwt.getClaim("authorization").asString();
            String status = jwt.getClaim("status").asString();

            return authorization.equals("ROLE_USER") && status.equals("활성");

        } catch (JWTVerificationException exception) {  // 검증 실패 시, 예외 처리
            return false;
        }
    }

    public static class Config { // // 필요한 전달할 설정
    }
}

