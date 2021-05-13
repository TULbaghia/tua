package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.UUID;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

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
     * @param login login konta, które ma zostać zablokowane.
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
     * Odblokowywuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać odblokowane.
     * @throws AppBaseException gdy nie udało się odblokowanie konta.
     */
    @RolesAllowed("unblockAccount")
    public void unblockAccount(String login) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setEnabled(true);
        accountFacade.edit(account);
        emailSender.sendUnlockAccountEmail(account.getFirstname(), account.getLogin());
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

    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws CodeException             gdy podany kod został już zużyty lub wygasł
     * @throws AccountException                 gdy konto zostało już aktywowane
     * @throws AppBaseException                 gdy utrwalenie potwierdzenia się nie powiodło
     */
    @PermitAll
    public void confirm(String code) throws AppBaseException {
        PendingCode pendingCode = pendingCodeFacade.findByCode(code);
        if(pendingCode.getCodeType() != CodeType.ACCOUNT_ACTIVATION){
            throw CodeException.codeInvalid();
        }
        if (pendingCode.isUsed()) {
            throw CodeException.codeExpired();
        }
        var account = pendingCode.getAccount();
        if (account.isConfirmed()) {
            throw AccountException.alreadyActivated();
        }
        account.setConfirmed(true);
        pendingCode.setUsed(true);
        pendingCodeFacade.edit(pendingCode);
        accountFacade.edit(account);
    }

    /**
     * Aktualizuje dane związane z niepoprawnym uwierzytelnieniem oraz blokuje konto po trzech nieudanych próbach uwierzytelnienia.
     *
     * @param login login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate data nieudanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    public void updateInvalidAuth(String login, String ipAddress, Date authDate) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        Inet4Address address = Inet4AddressFromString(ipAddress);
        account.setLastFailedLoginIpAddress (address);
        account.setLastFailedLoginDate(authDate);
        int incorrectLoginAttempts = account.getFailedLoginAttemptsCounter() + 1;
        if(incorrectLoginAttempts == 3) {
            account.setEnabled(false);
            emailSender.sendLockAccountEmail(account.getFirstname(), login);
            incorrectLoginAttempts = 0;
        }
        account.setFailedLoginAttemptsCounter(incorrectLoginAttempts);

        accountFacade.edit(account);
    }

    /**
     * Aktualizuje dane związane z poprawnym uwierzytelnieniem się użytkownika
     *
     * @param login login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate data udanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    public void updateValidAuth(String login, String ipAddress, Date authDate) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        Inet4Address address = Inet4AddressFromString(ipAddress);
        account.setLastSuccessfulLoginIpAddress(address);
        account.setLastSuccessfulLoginDate(authDate);
        account.setFailedLoginAttemptsCounter(0);

        accountFacade.edit(account);
    }

    private Inet4Address Inet4AddressFromString(String ipAddress) {
        Inet4Address address = null;
        try {
            for(InetAddress addr : Inet4Address.getAllByName(ipAddress)){
                if(addr instanceof Inet4Address) {
                    address = (Inet4Address) addr;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * Zwraca encję użytkownika
     *
     * @param login login użytkownika
     * @return encja użytkownika
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @RolesAllowed({"getOtherAccountInfo", "getOwnAccountInfo", "addAccessLevel", "deleteAccessLevel"})
    public Account getAccountByLogin(String login) throws AppBaseException {
        return accountFacade.findByLogin(login);
    }

    /**
     * Zmienia dane użytkownika wykonującego przypadek użycia w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param account obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed({"editOwnAccountDetails", "editOtherAccountDetails"})
    public void editAccountDetails(Account account) throws AppBaseException {
        accountFacade.edit(account);
    }

    /**
     * Wyszukuje obiekt Acccount o podanym loginie.
     *
     * @param login login wyszukiwanego konta użytkownika.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PermitAll
    public Account findByLogin(String login) throws AppBaseException {
        return accountFacade.findByLogin(login);
    }
}
