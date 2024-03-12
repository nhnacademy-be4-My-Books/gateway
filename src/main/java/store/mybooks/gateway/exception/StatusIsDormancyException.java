package store.mybooks.gateway.exception;

/**
 * packageName    : store.mybooks.front.auth.exception<br>
 * fileName       : StatusIsNoeActiceException<br>
 * author         : masiljangajji<br>
 * date           : 3/3/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/3/24        masiljangajji       최초 생성
 */
public class StatusIsDormancyException extends RuntimeException{
    public StatusIsDormancyException() {
        super("휴면 상태입니다");
    }
}
