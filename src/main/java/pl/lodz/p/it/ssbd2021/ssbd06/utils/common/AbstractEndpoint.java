package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

/**
 * Do sprawdzania eTagu, metoda przed rozpoczeciem transakcji, metoda po zakonczeniu transakcji.
 */
public abstract class AbstractEndpoint {

    private String transactionId;
    private boolean lastTransactionRollback;

    public boolean isLastTransactionRollback() {
        return lastTransactionRollback;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
