package store.mybooks.gateway.validator;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Arrays;
import store.mybooks.gateway.exception.ForbiddenAccessException;
import store.mybooks.gateway.exception.InvalidStatusException;

/**
 * packageName    : store.mybooks.gateway.validator<br>
 * fileName       : TokenValidator<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */

public class TokenValidator {

    private static final JWTVerifier jwtVerifier;

    static {
        Algorithm algorithm = Algorithm.HMAC512("이승재"); // todo 키 메니저 달아서 암호화
        jwtVerifier = JWT.require(algorithm).build();
    }

    public static void isValidAuthority(DecodedJWT jwt, String userStatus, String... authority) {

        String authorization = jwt.getClaim("authority").asString();
        String status = jwt.getClaim("status").asString();


        // 활성상태아니면 throw
        if (!status.equals(userStatus)) {
            throw new InvalidStatusException();
        }

        // 권한 확인
        if (!Arrays.asList(authority).contains(authorization)) {
            throw new ForbiddenAccessException();
        }

    }

    public static DecodedJWT isValidToken(String token) throws JWTVerificationException {
        // 토큰이 유효한지 , 만료되지 않았는지
        return jwtVerifier.verify(token);
    }

}
