package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący encji Account
 */
public class AccountException extends AppBaseException {
    private static final String ACCOUNT_LOGIN_INVALID = "exception.account.login_invalid";
    private static final String ACCOUNT_CONTACT_NUMBER = "exception.account.contact_number";
    private static final String ACCOUNT_ALREADY_ACTIVATED = "exception.account_already_activated.already_activated";
    private static final String ACCOUNT_SAME_PASSWORD = "exception.account_same_password.account_same_password";
    private static final String ACCOUNT_PASSWORDS_DONT_MATCH = "exception.passwords_dont_match.passwords_dont_match";
    private static final String ACCOUNT_NOT_ENABLED = "exception.account_not_enabled.account_not_enabled";
    private static final String ACCOUNT_NOT_CONFIRMED = "exception.account_not_confirmed.account_not_confirmed";
    private static final String ACCOUNT_EMAIL_INVALID = "exception.account.email_invalid";
    private static final String ACCOUNT_IS_CONFIRMED = "exception.account.account_confirmed";

    private AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    private AccountException(String message) {
        super(message);
    }

    /**
     * Wyjątek reprezentuje błąd podczas zakładania konta związany z zajętym loginem.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AccountException
     */
    public static AccountException loginExists(Throwable cause) {
        return new AccountException(ACCOUNT_LOGIN_INVALID, cause);
    }

    /**
     * Wyjątek reprezentuje błąd podczas zakładania konta związany z nieprawidłowym numerem kontaktowym.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AccountException
     */
    public static AccountException contactNumberException(Throwable cause) {
        return new AccountException(ACCOUNT_CONTACT_NUMBER, cause);
    }

    /**
     * Wyjątek reprezentuje błąd podczas zakładania konta związany z zajętym adresem email.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AccountException
     */
    public static AccountException emailExists(Throwable cause) {
        return new AccountException(ACCOUNT_EMAIL_INVALID, cause);
    }

    /**
     * Wyjątek reprezentuje błąd podczas zakładania konta związany z zajętym adresem email.
     *
     * @return wyjątek AccountException
     */
    public static AccountException emailExists() {
        return new AccountException(ACCOUNT_EMAIL_INVALID);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika zostało wcześniej już aktywowane.
     * @return wyjątek AccountException
     */
    public static AccountException alreadyActivated() {
        return new AccountException(ACCOUNT_ALREADY_ACTIVATED);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że podane nowe hasło jest takie samo jak stare hasło.
     * @return wyjątek AccountException
     */
    public static AccountException samePassword() {
        return new AccountException(ACCOUNT_SAME_PASSWORD);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że podane nowe hasła nie są identyczne.
     * @return wyjątek AccountException
     */
    public static AccountException passwordsDontMatch() {
        return new AccountException(ACCOUNT_PASSWORDS_DONT_MATCH);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika nie jest aktywowane.
     * @return wyjątek AccountException
     */
    public static AccountException notEnabled() {
        return new AccountException(ACCOUNT_NOT_ENABLED);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika nie jest potwierdzone.
     * @return wyjątek AccountException
     */
    public static AccountException notConfirmed() {
        return new AccountException(ACCOUNT_NOT_CONFIRMED);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie usunięcia konta ze względu na to, że konto zostało zweryfikowane
     * @return wyjątek typu  AccountException
     */
    public static AccountException confirmed(){
        return new AccountException(ACCOUNT_IS_CONFIRMED);
    }
}
