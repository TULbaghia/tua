package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailSender emailSender;

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
     * Rejestruje konto użytkownika oraz wysyła żeton na adres e-mail.
     *
     * @param account obiekt encji konta
     * @throws AppBaseException podczas wystąpienia błędu związanego z istniejącym loginem lub problemem z bazą danych.
     */
    @PermitAll
    public void register(Account account) throws AppBaseException {
        account.setPassword(new PasswordHasher().generate(account.getPassword().toCharArray()));
        account.setEnabled(true);
        account.setConfirmed(false);
        account.setCreatedBy(account);

        PendingCode pendingCode = new PendingCode();
        pendingCode.setCode(UUID.randomUUID().toString());
        pendingCode.setUsed(false);
        pendingCode.setAccount(account);
        pendingCode.setCodeType(CodeType.ACCOUNT_ACTIVATION);
        pendingCode.setCreatedBy(account);

        account.getPendingCodeList().add(pendingCode);

        accountFacade.create(account);
        emailSender.sendActivationEmail(account.getFirstname(), account.getLogin(), pendingCode.getCode());
    }
}
