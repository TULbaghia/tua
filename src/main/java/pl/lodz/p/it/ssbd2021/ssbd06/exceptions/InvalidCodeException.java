package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class InvalidCodeException extends AppBaseException {
    public InvalidCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCodeException(String message) {
        super(message);
    }
}
