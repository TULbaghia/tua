package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje bład występujący podczas przeprowadzania transakcji aplikacyjnych
 */
public class TransactionException extends AppBaseException {

    private static final String UNEXPECTED_FAIL = "exception.transaction_exception.unexpected_fail";

    private TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    private TransactionException(String message) {
        super(message);
    }

    /**
     * Wyjątek reprezentujący błąd występujacy po przekroczeniu liczby dozwolonych powtórzeń transakcji aplikacyjnej
     *
     * @return wyjątek typu TransactionException
     */
    public static TransactionException unexpectedFail() {
        return new TransactionException(UNEXPECTED_FAIL);
    }
}
