package store.mybooks.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : store.mybooks.gateway.controller
 * fileName       : TestController
 * author         : newjaehun
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        newjaehun       최초 생성
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
