package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class TransactionException extends AppBaseException {

    private static final String UNEXPECTED_FAIL = "exception.transaction_exception.unexpected_fail";

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(String message) {
        super(message);
    }

    public static AppBaseException unexpectedFail() {
        return new AppBaseException(UNEXPECTED_FAIL);
    }
}
