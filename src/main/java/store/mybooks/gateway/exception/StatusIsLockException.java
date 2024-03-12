package store.mybooks.gateway.exception;

/**
 * packageName    : store.mybooks.front.auth.exception<br>
 * fileName       : StatusIsLockException<br>
 * author         : masiljangajji<br>
 * date           : 3/11/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/11/24        masiljangajji       최초 생성
 */
public class StatusIsLockException extends RuntimeException{

    public StatusIsLockException() {
        super("잠금상태 입니다");
    }

}
