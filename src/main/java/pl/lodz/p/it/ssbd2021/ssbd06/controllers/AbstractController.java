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
    protected void repeat(MethodExecutor executor, CallingClass callingClass) throws AppBaseException {
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
//                TODO: logowanie do dziennika
                log.warning(String.format("Method invoke in class: %s is being repeated ", callingClass.getClass().getName()));
            }
            callMethodCounter++;
        } while (rollback && callMethodCounter <= transactionLimit);

        if(callMethodCounter > transactionLimit) {
            throw TransactionException.unexpectedFail();
        }
    }

    @FunctionalInterface
    public interface MethodExecutor {
        void run() throws AppBaseException;
    }
//    @FunctionalInterface
//    public interface CallingClass {
//        void run() throws AppBaseException;
//    }
}
