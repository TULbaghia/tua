package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AuthValidationException extends AppBaseException {
    private static final String INVALID_CREDENTIALS = "exception.auth_validation.invalid_credentials";

    public AuthValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthValidationException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący błąd uwierzytelnienia.
     * @return wyjątek AuthValidationException
     */
    public static AuthValidationException invalidCredentials() {
        return new AuthValidationException(INVALID_CREDENTIALS);
    }
}
