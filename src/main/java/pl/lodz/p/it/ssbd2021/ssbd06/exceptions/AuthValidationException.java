package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AuthValidationException extends AppBaseException {
    public AuthValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthValidationException(String message) {
        super(message);
    }
}
