package store.mybooks.gateway.utils;

import org.springframework.web.server.ServerWebExchange;

/**
 * packageName    : store.mybooks.gateway.utils<br>
 * fileName       : Utils<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
public class HttpUtils {

    public static String getAuthorizationHeaderValue(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("Authorization");
    }
    public static String getUserAgentHeaderValue(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("UserAgent");
    }

    public static String getUserIpHeaderValue(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("UserIp");
    }

    public static String getPath(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getPath();
    }

}
