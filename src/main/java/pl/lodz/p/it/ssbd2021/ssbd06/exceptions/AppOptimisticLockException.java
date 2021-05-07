package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class AppOptimisticLockException extends AppBaseException {

    private static final String OPTIMISTIC_LOCK = "exception.app_optimistic_lock.optimistic_lock";

    public AppOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppOptimisticLockException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący konflikt z mechanizmem blokady optymistycznej.
     * @return wyjątek AppBaseException
     */
    public static AppBaseException optimisticLockException() {
        return new AppBaseException(OPTIMISTIC_LOCK);
    }

    /**
     * Tworzy wyjątek reprezentujący konflikt z mechanizmem blokady optymistycznej.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AppBaseException
     */
    public static AppBaseException optimisticLockException(Throwable cause) {
        return new AppBaseException(OPTIMISTIC_LOCK, cause);
    }
}
