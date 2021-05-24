package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.util.logging.Level;

/**
 * Klasa abstrakcyjna definiująca metody wspólne dla wszystkich kontrolerów.
 * Służy do powtarzania transakcji aplikacyjnych.
 */
@Log
public abstract class AbstractController {

    @Context
    ServletContext servletContext;

    /**
     * Metoda powtarzająca transakcję aplikacyjną
     *
     * @param executor     wyrażenie lambda wywołane po użyciu metody run()
     * @param callingClass klasa wywołująca metodę przetwarzaną transakcyjnie
     * @throws AppBaseException w momencie niepowodzenia
     */
    protected void repeat(VoidMethodExecutor executor, CallingClass callingClass) throws AppBaseException {
        int transactionLimit = Integer.parseInt(getInitialParameter("repeatTransactionNumber"));
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
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{callingClass.getTransactionId(), callMethodCounter});
            }
            callMethodCounter++;
        } while (rollback && callMethodCounter <= transactionLimit);

        if (callMethodCounter > transactionLimit) {
            throw TransactionException.unexpectedFail();
        }
    }

    /**
     * Metoda powtarzająca transakcję aplikacyjną
     *
     * @param executor     wyrażenie lambda wywołane po użyciu metody run()
     * @param callingClass klasa wywołująca metodę przetwarzaną transakcyjnie
     * @param <T>          typ zwracany
     * @return wartość zwracana przez funkcję przekazaną w parametrze executor
     * @throws AppBaseException w momencie niepowodzenia
     */
    protected <T> T repeat(ReturnMethodExecutor<T> executor, CallingClass callingClass) throws AppBaseException {
        int transactionLimit = Integer.parseInt(getInitialParameter("repeatTransactionNumber"));
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
                log.log(Level.WARNING, "Transaction with ID: {0} is being repeated {1} time",
                        new Object[]{callingClass.getTransactionId(), callMethodCounter});
            }
            callMethodCounter++;
        } while (rollback && callMethodCounter <= transactionLimit);

        if (callMethodCounter > transactionLimit) {
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

    /**
     * Pozwala odczytać parametr uruchomieniowy z desktryptora wdrożenia
     *
     * @param paramName nazwa parametru, który chcemy wczytać
     * @return wartość wczytanego parametru
     */
    private String getInitialParameter(String paramName) {
        return servletContext.getInitParameter(paramName);
    }
}
