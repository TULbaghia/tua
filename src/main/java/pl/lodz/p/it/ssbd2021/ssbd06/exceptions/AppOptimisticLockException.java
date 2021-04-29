package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AppOptimisticLockException extends AppBaseException {

    public AppOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppOptimisticLockException(String message) {
        super(message);
    }
}
