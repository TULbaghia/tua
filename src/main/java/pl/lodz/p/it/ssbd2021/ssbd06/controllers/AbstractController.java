package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.PropertyReader;

import javax.ejb.EJBTransactionRolledbackException;

/**
 * Do powtarzania transakcji aplikacyjnych.
 */
@Log
public abstract class AbstractController {

    /**
     * Metoda powtarzająca transakcję aplikacyjną
     *
     * @param executor wyrażenie lambda wywołane po użyciu metody run()
     * @param callingClass klasa wywołująca metodę przetwarzaną transakcyjnie
     * @throws AppBaseException w momencie niepowodzenia
     */
    protected void repeat(VoidMethodExecutor executor, CallingClass callingClass) throws AppBaseException {
        int transactionLimit = Integer.parseInt(PropertyReader.getBundleProperty("config", "transaction_limit"));
        int callMethodCounter = 0;
        boolean rollback;
        do {
            try {
                executor.run();
                rollback = callingClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException e) {
                rollback = true;
            }
            if (callMethodCounter > 0) {
                log.warning(String.format("Transaction: %s is being repeated %s time", callingClass.getTransactionId(), callMethodCounter));
            }
            callMethodCounter++;
        } while (rollback && callMethodCounter <= transactionLimit);

        if(callMethodCounter > transactionLimit) {
            throw TransactionException.unexpectedFail();
        }
    }

    /**
     * Metoda powtarzająca transakcję aplikacyjną
     *
     * @param executor wyrażenie lambda wywołane po użyciu metody run()
     * @param callingClass klasa wywołująca metodę przetwarzaną transakcyjnie
     * @param <T> typ zwracany
     * @return wartość zwracana przez funkcję przekazaną w parametrze executor
     * @throws AppBaseException w momencie niepowodzenia
     */
    protected <T> T repeat(ReturnMethodExecutor<T> executor, CallingClass callingClass) throws AppBaseException {
        int transactionLimit = Integer.parseInt(PropertyReader.getBundleProperty("config", "transaction_limit"));
        int callMethodCounter = 0;
        boolean rollback;
        T result = null;
        do {
            try {
                result = executor.run();
                rollback = callingClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException e) {
                rollback = true;
            }
            if (callMethodCounter > 0) {
                log.warning(String.format("Transaction: %s is being repeated %s time", callingClass.getTransactionId(), callMethodCounter));
            }
            callMethodCounter++;
        } while (rollback && callMethodCounter <= transactionLimit);

        if(callMethodCounter > transactionLimit) {
            throw TransactionException.unexpectedFail();
        }
        return result;
    }

    /**
     * Interfejs funkcyjny przeznaczony dla metod typu void
     */
    @FunctionalInterface
    public interface VoidMethodExecutor {
        void run() throws AppBaseException;
    }

    /**
     * Interfejs funkcyjny przeznaczony dla metod zwracających obiekt określonego typu
     *
     * @param <T> typ zwracanego przez metodę obiektu
     */
    @FunctionalInterface
    public interface ReturnMethodExecutor<T> {
        T run() throws AppBaseException;
    }
}
