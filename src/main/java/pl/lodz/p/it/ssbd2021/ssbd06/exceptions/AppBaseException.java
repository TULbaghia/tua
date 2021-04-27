package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class AppBaseException extends Exception {
    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppBaseException(String message) {
        super(message);
    }
}
