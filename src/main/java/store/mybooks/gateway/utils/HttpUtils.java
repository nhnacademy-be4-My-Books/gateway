package store.mybooks.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
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

    public static String getPath(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getPath();
    }

}
