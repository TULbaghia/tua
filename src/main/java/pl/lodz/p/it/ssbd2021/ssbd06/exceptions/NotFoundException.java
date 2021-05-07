package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class NotFoundException extends AppBaseException {

    private static final String ACCOUNT_NOT_FOUND = "exception.not_found_exception.account_not_found";
    private static final String PENDING_CODE_NOT_FOUND = "exception.not_found_exception.pending_code_not_found";

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji account.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek EmailException
     */
    public static NotFoundException accountNotFound(Throwable cause) {
        return new NotFoundException(ACCOUNT_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji pending code.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek EmailException
     */
    public static NotFoundException pendingCodeNotFound(Throwable cause) {
        return new NotFoundException(PENDING_CODE_NOT_FOUND, cause);
    }
}
