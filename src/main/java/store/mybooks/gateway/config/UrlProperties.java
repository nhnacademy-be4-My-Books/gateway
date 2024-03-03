package store.mybooks.gateway.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * packageName    : store.mybooks.gateway.config
 * fileName       : PropertiesConfig
 * author         : damho-lee
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "url.name")
public class UrlProperties {
    private String auth;
    private final KeyConfig keyConfig;

    public void setAuth(String auth) {
        this.auth = keyConfig.keyStore(auth);
    }
}
