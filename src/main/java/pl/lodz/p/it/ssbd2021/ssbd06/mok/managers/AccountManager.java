package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CodeConfig;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailSender emailSender;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private CodeConfig config;

    private static int RESET_EXPIRATION_MINUTES;

    @PostConstruct
    private void init() {
        RESET_EXPIRATION_MINUTES = config.getResetExpirationMinutes();
    }

    public Account findByLogin(String login) throws AppBaseException {
        return accountFacade.findByLogin(login);
    }

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
     * Resetuje hasło użytkownika
     *
     * @param password nowe hasło
     * @param code token służący resetowniu
     * @throws AppBaseException w przypadku nieudanej operacji
     */
    @PermitAll
    public void resetPassword(String password, String code) throws AppBaseException {
        PendingCode resetCode = pendingCodeFacade.findByCode(code);
        Account account = accountFacade.findByLogin(resetCode.getAccount().getLogin());
        if(!account.isConfirmed()) throw AccountException.notConfirmed();
        if(!account.isEnabled()) throw AccountException.notEnabled();

        if(resetCode.getCodeType().toString() != "PASSWORD_RESET") {
            throw CodeException.codeInvalid();
        }
        Date expirationTime = new Date(resetCode.getCreationDate().getTime() + (RESET_EXPIRATION_MINUTES * 60000));
        Date localTime = Timestamp.valueOf(LocalDateTime.now());
        if(localTime.after(expirationTime)) {
            throw CodeException.codeExpired();
        }
        resetCode.setUsed(true);
        pendingCodeFacade.edit(resetCode);
        changePassword(account, password);
    }
}
