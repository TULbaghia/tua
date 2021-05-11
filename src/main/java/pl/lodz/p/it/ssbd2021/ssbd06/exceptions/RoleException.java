package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class RoleException extends AppBaseException {

    private static final String ALREADY_REVOKED = "exception.role_exception.already_revoked";

    private RoleException(String message, Throwable cause) {
        super(message, cause);
    }

    private RoleException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek, gdy rola jest już odebrana użytkownikowi.
     *
     * @return wyjątek RoleException
     */
    public static RoleException alreadyRevoked() {
        return new RoleException(ALREADY_REVOKED);
    }
}
