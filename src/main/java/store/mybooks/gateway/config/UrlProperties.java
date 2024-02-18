package store.mybooks.gateway.config;

import lombok.Getter;
import lombok.Setter;
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
@Component
@ConfigurationProperties(prefix = "url.name")
@Getter
@Setter
public class UrlProperties {
    private String auth;
    private String resource1;
}
