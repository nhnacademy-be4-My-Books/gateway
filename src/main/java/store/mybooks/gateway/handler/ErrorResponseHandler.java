package store.mybooks.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * packageName    : store.mybooks.gateway.handler<br>
 * fileName       : ErrorResponseHandler<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */

public class ErrorResponseHandler {

    private ErrorResponseHandler() {
    }

    public static Mono<Void> handleInvalidToken(ServerWebExchange exchange, HttpStatus httpStatus,
                                                String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorMessage.getBytes())));
    }


}