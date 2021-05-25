package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.security.Principal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Klasa abstrakcyjna służąca do sprawdzania integralności wersji, dostarcza metody wykonywane przed rozpoczęciem
 * transakcji oraz po jej zakończeniu.
 */
@Log
public abstract class AbstractEndpoint {

    @Inject
    private MessageSigner messageSigner;

    @Context
    private HttpHeaders httpHeaders;

    @Inject
    private SecurityContext securityContext;

    @Getter(AccessLevel.PROTECTED)
    @Inject
    private HttpServletRequest httpServletRequest;

    @Getter
    private String transactionId;

    @Getter
    private boolean lastTransactionRollback;

    /**
     * Weryfikuje integralność danych
     *
     * @param signable obiekt typu Signable.
     * @return wynik weryfikacji.
     */
    public boolean verifyIntegrity(Signable signable) {
        String valueFromHeader = httpHeaders.getRequestHeader("If-Match").get(0);
        String valueFromSigner = messageSigner.sign(signable);
        return valueFromSigner.equals(valueFromHeader);
    }

    /**
     * Metoda wywoływana przed rozpoczęciem transakcji.
     */
    @AfterBegin
    public void afterBegin() {
        transactionId =
                Long.toString(System.currentTimeMillis()) + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        log.log(Level.INFO, "Transaction with ID:{0} started in {1}, account: {2}",
                new Object[]{transactionId, this.getClass().getName(), getLogin()});
    }

    /**
     * Metoda wywoływana po zakończeniu transakcji.
     *
     * @param committed zawiera informacje czy transakcja została zatwierdzona.
     */
    @AfterCompletion
    public void afterCompletion(boolean committed) {
        lastTransactionRollback = !committed;
        log.log(Level.INFO, "Transaction with ID:{0} ended in {1}, result: {2}, account: {3}",
                new Object[]{transactionId, this.getClass().getName(), getResult(), getLogin()});
    }

    /**
     * Zwraca login użytkownika
     *
     * @return login użytkownika lub 'Guest' dla gościa
     */
    protected String getLogin() {
        Principal principal = securityContext.getCallerPrincipal();
        if (principal != null) {
            return principal.getName();
        }
        return "Guest";
    }

    /**
     * Informuje czy ostatnia trasakcja została zatwierdzona
     *
     * @return status zatwierdzenia ostatniej transakcji
     */
    private String getResult() {
        if (lastTransactionRollback) {
            return "rollback";
        }
        return "commit";
    }
}
