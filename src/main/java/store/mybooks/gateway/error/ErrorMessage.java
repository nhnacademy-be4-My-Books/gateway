package store.mybooks.gateway.error;

import lombok.Getter;

/**
 * packageName    : store.mybooks.gateway.error<br>
 * fileName       : ErrorMessage<br>
 * author         : masiljangajji<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/2/24        masiljangajji       최초 생성
 */
@Getter
public enum ErrorMessage {
    TOKEN_EXPIRED("Token has expired"),
    INVALID_TOKEN("Invalid token"),
    INVALID_ACCESS("Access forbidden"),
    STATUS_IS_DORMANT_EXCEPTION("User Status is DORMANT"),
    STATUS_IS_LOCK_EXCEPTION("User Status is Lock");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

}
