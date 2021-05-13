package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class RoleException extends AppBaseException {

    private static final String ALREADY_GRANTED = "exception.role_exception.already_granted";
    private static final String UNSUPPORTED_ACCESS_LEVEL = "exception.role_exception.unsupported_access_level";

    private RoleException(String message, Throwable cause) {
        super(message, cause);
    }

    private RoleException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek, gdy rola jest już przydzielona użytkownikowi.
     *
     * @return wyjątek RoleException
     */
    public static RoleException alreadyGranted() {
        return new RoleException(ALREADY_GRANTED);
    }

    /**
     * Tworzy wyjątek, gdy poziom dostępu nie jest obsługiwany.
     *
     * @return wyjątek RoleException
     */
    public static RoleException unsupportedAccessLevel() {
        return new RoleException(UNSUPPORTED_ACCESS_LEVEL);
    }
}
