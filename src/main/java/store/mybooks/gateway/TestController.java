package store.mybooks.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : store.mybooks.gateway
 * fileName       : TestController
 * author         : minsu11
 * date           : 2/14/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/14/24        minsu11       최초 생성
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test2";
    }
}
