package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountAlreadyActivatedException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeExpiredException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailSender emailSender;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login ogin konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @RolesAllowed("blockAccount")
    public void blockAccount(String login) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setEnabled(false);
        account.setFailedLoginAttemptsCounter(0);
        accountFacade.edit(account);
        emailSender.sendLockAccountEmail(account.getFirstname(), login);
    }

    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws CodeExpiredException             gdy podany kod został już zużyty lub wygasł
     * @throws AccountAlreadyActivatedException gdy konto zostało już aktywowane
     * @throws AppBaseException                 gdy utrwalenie potwierdzenia się nie powiodło
     */
    public void confirm(String code) throws AppBaseException {
        var pendingCode = pendingCodeFacade.findByCode(code);
        if (pendingCode.isUsed()) {
            throw new CodeExpiredException("Code has been used or expired");
        }
        var account = pendingCode.getAccount();
        if (account.isConfirmed()) {
            throw new AccountAlreadyActivatedException("Account already activated");
        }
        account.setConfirmed(true);
        pendingCode.setUsed(true);
        pendingCodeFacade.edit(pendingCode);
        accountFacade.edit(account);
    }
}
