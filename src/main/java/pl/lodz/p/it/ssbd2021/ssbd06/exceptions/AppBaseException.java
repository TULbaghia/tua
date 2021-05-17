package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

import javax.ejb.ApplicationException;

/**
 * Reprezentuje bazowy wyjątek aplikacyjny, jego wystąpienie skutkuje odwołaniem transakcji aplikacyjnej
 */
@ApplicationException(rollback = true)
public class AppBaseException extends Exception {
    protected AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AppBaseException(String message) {
        super(message);
    }
}
