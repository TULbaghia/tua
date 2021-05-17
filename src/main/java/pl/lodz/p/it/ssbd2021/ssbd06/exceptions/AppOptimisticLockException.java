package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący blokady optymistycznej
 */
public class AppOptimisticLockException extends AppBaseException {

    private static final String OPTIMISTIC_LOCK = "exception.app_optimistic_lock.optimistic_lock";

    private AppOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    private AppOptimisticLockException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący konflikt z mechanizmem blokady optymistycznej.
     * @return wyjątek AppBaseException
     */
    public static AppOptimisticLockException optimisticLockException() {
        return new AppOptimisticLockException(OPTIMISTIC_LOCK);
    }

    /**
     * Tworzy wyjątek reprezentujący konflikt z mechanizmem blokady optymistycznej.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek AppBaseException
     */
    public static AppOptimisticLockException optimisticLockException(Throwable cause) {
        return new AppOptimisticLockException(OPTIMISTIC_LOCK, cause);
    }
}
