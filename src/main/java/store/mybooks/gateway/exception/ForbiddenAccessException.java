package store.mybooks.gateway.exception;

/**
 * packageName    : store.mybooks.gateway.exception<br>
 * fileName       : TokenNotValidException<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException() {
        super();
    }
}
