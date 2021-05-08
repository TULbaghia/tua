package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AccountException extends AppBaseException {
    private static final String ACCOUNT_LOGIN_EXISTS = "exception.account.login_exists";
    private static final String ACCOUNT_CONTACT_NUMBER = "exception.account.contact_number";
    private static final String ACCOUNT_ALREADY_ACTIVATED = "exception.account_already_activated.already_activated";
    private static final String ACCOUNT_NOT_ENABLED = "exception.account_not_enabled.account_not_enabled";
    private static final String ACCOUNT_NOT_CONFIRMED = "exception.account_not_confirmed.account_not_confirmed";

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountException(String message) {
        super(message);
    }

    /**
     * Wyjątek reprezentuje błąd podczas zakładania konta związany z zajętym loginem.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AccountException
     */
    public static AccountException loginExists(Throwable cause) {
        return new AccountException(ACCOUNT_LOGIN_EXISTS, cause);
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
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika zostało wcześniej już aktywowane.
     * @return wyjątek AccountException
     */
    public static AccountException alreadyActivated() {
        return new AccountException(ACCOUNT_ALREADY_ACTIVATED);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika zostało nie jest aktywowane.
     * @return wyjątek AccountException
     */
    public static AccountException notEnabled() {
        return new AccountException(ACCOUNT_NOT_ENABLED);
    }

    /**
     *  Tworzy wyjątek reprezentujący niepowodzenie operacji ze względu na to, że konto użytkownika zostało nie jest potwierdzone.
     * @return wyjątek AccountException
     */
    public static AccountException notConfirmed() {
        return new AccountException(ACCOUNT_NOT_CONFIRMED);
    }
}
