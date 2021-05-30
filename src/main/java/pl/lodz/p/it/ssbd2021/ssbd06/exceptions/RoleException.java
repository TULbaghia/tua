package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas zarządzania rolami w aplikacji
 */
public class RoleException extends AppBaseException {

    private static final String ALREADY_REVOKED = "exception.role_exception.already_revoked";
    private static final String ALREADY_GRANTED = "exception.role_exception.already_granted";
    private static final String UNSUPPORTED_ACCESS_LEVEL = "exception.role_exception.unsupported_access_level";
    private static final String UNSUPPORTED_ROLE_COMBINATION = "exception.role_exception.unsupported_role_combination";
    private static final String ACCOUNT_NOT_CONFIRMED = "exception.role_exception.account_not_confirmed";

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

    /**
     * Tworzy wyjątek, gdy poziomy dostępu użytkownika wykluczają się.
     *
     * @return wyjątek RoleException
     */
    public static RoleException unsupportedRoleCombination() {
        return new RoleException(UNSUPPORTED_ROLE_COMBINATION);
    }

    /**
     * Tworzy wyjątek, gdy występuje próba zmiany roli dla konta, które nie jest potwierdzone.
     *
     * @return wyjątek RoleException
     */
    public static RoleException accountNotConfirmed() {
        return new RoleException(ACCOUNT_NOT_CONFIRMED);
    }
}
