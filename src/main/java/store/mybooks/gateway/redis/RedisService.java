package store.mybooks.gateway.redis;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : store.mybooks.authorization.redis<br>
 * fileName       : RedisService<br>
 * author         : masiljangajji<br>
 * date           : 3/7/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/24        masiljangajji       최초 생성
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 값 찾아오기 , 없으면 false;
    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return null;
        }
        return (String) values.get(key);
    }

    @Transactional(readOnly = true)
    public void isValidateUser(String key){

        // 레디스에 유저 아이디 담은 정보가 없다면 , 이미 로그아웃 한 것 따라서 유효하지 않은 토큰으로 보겠음
        if (Objects.isNull(getValues(key))) {
            log.warn("레디스에 유저 아이디 담은 정보가 없음");
            throw new JWTVerificationException("Logout Token");
        }

    }

}
