package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AccountAlreadyActivatedException extends AppBaseException{
    public AccountAlreadyActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountAlreadyActivatedException(String message) {
        super(message);
    }
}
