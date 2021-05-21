package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas uwierzytelniania
 */
public class AuthValidationException extends AppBaseException {
    private static final String INVALID_CREDENTIALS = "exception.auth_validation.invalid_credentials";

    private AuthValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    private AuthValidationException(String message) {
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
