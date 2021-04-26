package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class EmailException extends AppBaseException {
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(String message) {
        super(message);
    }
}
