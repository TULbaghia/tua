package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ClientData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.ThemeColor;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Odpowiada za przetwarzanie logiki biznesowej związanej z modelem obsługi kont.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailSender emailSender;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Config codeConfig;

    @Context
    ServletContext servletContext;

    private static int RESET_EXPIRATION_MINUTES;

    private static int INCORRECT_LOGIN_ATTEMPTS_LIMIT;


    @PostConstruct
    private void init() {
        RESET_EXPIRATION_MINUTES = codeConfig.getResetExpirationMinutes();
        INCORRECT_LOGIN_ATTEMPTS_LIMIT = Integer
                .parseInt(servletContext.getInitParameter("incorrectLoginAttemptsLimit"));
    }

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
        account.setModifiedBy(getCurrentUser());
        account.setEnableModificationDate(Date.from(Instant.now()));
        account.setEnableModificationBy(getCurrentUser());
        accountFacade.edit(account);
        emailSender.sendLockAccountEmail(account);
    }

    /**
     * Odblokowuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać odblokowane.
     * @throws AppBaseException gdy nie udało się odblokowanie konta.
     */
    @RolesAllowed("unblockAccount")
    public void unblockAccount(String login) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setEnabled(true);
        account.setFailedLoginAttemptsCounter(0);
        account.setModifiedBy(getCurrentUser());
        account.setEnableModificationDate(Date.from(Instant.now()));
        account.setEnableModificationBy(getCurrentUser());
        accountFacade.edit(account);
        emailSender.sendUnlockAccountEmail(account);
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
        emailSender.sendActivationEmail(account, pendingCode.getCode());
    }

    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws CodeException    gdy podany kod został już zużyty lub wygasł
     * @throws AccountException gdy konto zostało już aktywowane
     * @throws AppBaseException gdy utrwalenie potwierdzenia się nie powiodło
     */
    @PermitAll
    public void confirm(String code) throws AppBaseException {
        PendingCode pendingCode = pendingCodeFacade.findByCode(code);
        if (pendingCode.getCodeType() != CodeType.ACCOUNT_ACTIVATION) {
            throw CodeException.codeInvalid();
        }
        if (pendingCode.isUsed()) {
            throw CodeException.codeUsed();
        }
        Account account = pendingCode.getAccount();
        if (account.isConfirmed()) {
            throw AccountException.alreadyActivated();
        }

        ClientData clientData = new ClientData();
        clientData.setAccount(account);
        clientData.setCreatedBy(account);
        account.getRoleList().add(clientData);
        account.setConfirmModificationDate(Date.from(Instant.now()));
        account.setConfirmModificationBy(account);

        account.setConfirmed(true);
        pendingCode.setUsed(true);
        pendingCode.setModifiedBy(account);
        pendingCodeFacade.edit(pendingCode);
        accountFacade.edit(account);
        emailSender.sendActivationSuccessEmail(account);
    }

    /**
     * Aktualizuje dane związane z niepoprawnym uwierzytelnieniem oraz blokuje konto po trzech nieudanych próbach
     * uwierzytelnienia.
     *
     * @param login     login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate  data nieudanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    public void updateInvalidAuth(String login, String ipAddress, Date authDate) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setLastFailedLoginIpAddress(ipAddress);
        account.setLastFailedLoginDate(authDate);
        int incorrectLoginAttempts = account.getFailedLoginAttemptsCounter() + 1;
        if (incorrectLoginAttempts == INCORRECT_LOGIN_ATTEMPTS_LIMIT) {
            account.setEnabled(false);
            account.setEnableModificationDate(Date.from(Instant.now()));
            account.setEnableModificationBy(null);
            emailSender.sendLockAccountEmail(account);
        }
        account.setFailedLoginAttemptsCounter(incorrectLoginAttempts);

        accountFacade.edit(account);
    }

    /**
     * Aktualizuje dane związane z poprawnym uwierzytelnieniem się użytkownika
     *
     * @param login     login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate  data udanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    public void updateValidAuth(String login, String ipAddress, Date authDate, String lang) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setLastSuccessfulLoginIpAddress(ipAddress);
        account.setLastSuccessfulLoginDate(authDate);
        account.setFailedLoginAttemptsCounter(0);
        account.setLanguage(lang.substring(0, 2));

        accountFacade.edit(account);
    }

    /**
     * Zwraca listę wszystkich kont w systemie.
     *
     * @return lista kont
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getAllAccounts")
    public List<Account> getAllAccounts() throws AppBaseException {
        return new ArrayList<>(accountFacade.findAll());
    }

    /**
     * Zwraca dane konkretnego użytkownika
     *
     * @param login login użytkownika
     * @return dane konta wybranego użytkownika
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed({"getOwnAccountInfo", "getOtherAccountInfo"})
    public Account getAccount(String login) throws AppBaseException {
        return (accountFacade.findByLogin(login));
    }

    /**
     * Zmienia dane użytkownika wykonującego przypadek użycia w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param account obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed({"editOwnAccountDetails", "editOtherAccountDetails"})
    public void editAccountDetails(Account account) throws AppBaseException {
        account.setModifiedBy(getCurrentUser());
        accountFacade.edit(account);
    }

    /**
     * Wyszukuje obiekt Acccount o podanym loginie.
     *
     * @param login login wyszukiwanego konta użytkownika.
     * @return konto wyszukiwanego użytkownika
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @PermitAll
    public Account findByLogin(String login) throws AppBaseException {
        return accountFacade.findByLogin(login);
    }

    /**
     * Aktualizuje dane związane ze zmianą hasła przez użytkownika
     *
     * @param account  konto, dla którego zmienione ma zostać hasło
     * @param password nowe hasło
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed({"editOwnPassword", "editOtherPassword"})
    public void changePassword(Account account, String password) throws AppBaseException {
        if (PasswordHasher.check(password, account.getPassword())) {
            throw AccountException.samePassword();
        }
        account.setPassword(PasswordHasher.generate(password));
        account.setPasswordModificationDate(Date.from(Instant.now()));

        try {
            account.setPasswordModificationBy(getCurrentUser());
        } catch (NullPointerException e) {
            account.setPasswordModificationBy(account);
        }

        accountFacade.edit(account);
    }

    /**
     * Zwraca obecnego użytkownika
     *
     * @return konto aktualnego użytkownika
     * @throws AppBaseException gdy operacja się nie powiedzie
     */
    @RolesAllowed("editOwnPassword")
    public Account getCurrentUser() throws AppBaseException {
        return accountFacade.findByLogin(securityContext.getCallerPrincipal().getName());
    }

    /**
     * Resetuje hasło użytkownika
     *
     * @param password nowe hasło
     * @param code token służący resetowaniu
     * @throws AppBaseException w przypadku nieudanej operacji
     */
    @PermitAll
    public void resetPassword(String password, String code) throws AppBaseException {
        PendingCode resetCode = pendingCodeFacade.findByCode(code);
        Account account = accountFacade.findByLogin(resetCode.getAccount().getLogin());
        if (!account.isConfirmed()) {
            throw AccountException.notConfirmed();
        }
        if (!account.isEnabled()) {
            throw AccountException.notEnabled();
        }
        if (!resetCode.getCodeType().equals(CodeType.PASSWORD_RESET)) {
            throw CodeException.codeInvalid();
        }
        if (resetCode.isUsed()) throw CodeException.codeUsed();
        Date expirationTime = new Date(resetCode.getCreationDate().getTime() + (RESET_EXPIRATION_MINUTES * 60000L));
        Date localTime = Timestamp.valueOf(LocalDateTime.now());
        if (localTime.after(expirationTime)) {
            throw CodeException.codeExpired();
        }
        resetCode.setUsed(true);
        resetCode.setModifiedBy(account);
        pendingCodeFacade.edit(resetCode);
        changePassword(account, password);
    }

    /**
     * Zmienia adres email przypisany do konta użytkownika.
     * Wysyła żeton na aktualny adres email w celu potwierdzenia procesu zmiany adresu email na nowy.
     * Sprawdza, czy docelowy email nie jest loginem żadnego z aktualnych użytkowników.
     *
     * @param login    login użytkownika, którego email podlega zmianie.
     * @param newEmail nowy adres email.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed({"editOwnAccountEmail", "editOtherAccountEmail"})
    public void editAccountEmail(String login, String newEmail) throws AppBaseException {
        Account accountEmail = accountFacade.findByLogin(login);
        Account accountExists = null;

//        W bloku "try" następuje próba pozyskania z tabeli account konta, które posiada przypisany adres email,
//        identyczny jak adres email wykorzystywany w aktualnie przetwarzanym procesie zmiany tego zasobu.
//
//        1. W przypadku, gdy takie konto istnieje, zmienna accountExists nie będzie nullem, co powinno spowodować
//        zgłoszenie wyjątku AccountException.emailExists() - zgodnie z ograniczeniami, każdy adres email użytkownika
//        istniejący w bazie danych musi być unikalny.
//
//        2. W sytuacji, gdy nie następuje próba zmiany adresu email na taki, który obecnie posiada inny użytkownik,
//        wykonywanie metody accountFacade.findByEmail(newEmail), powinno zakończyć się wyjątkiem. Wyjątek ten - NotFoundException
//        jest dla nas informacją, że w bazie danych nie istnieje konflikt adresów email, a zatem proces zmiany można kontynuować,
//        dlatego też blok "catch" jest w tym przypadku pusty.

        try {
            accountExists = accountFacade.findByEmail(newEmail);
        } catch (NotFoundException ignore) {
        }

        if (accountExists != null) {
            throw AccountException.emailExists();
        }

        Set<PendingCode> unusedCodes = accountEmail.getPendingCodeList().stream()
                .filter(x -> x.getCodeType().equals(CodeType.EMAIL_CHANGE) && !x.isUsed())
                .collect(Collectors.toSet());

        if (!unusedCodes.isEmpty()) {
            unusedCodes.stream()
                    .filter(x -> x.getCodeType().equals(CodeType.EMAIL_CHANGE) && !x.isUsed())
                    .forEach(x -> x.setUsed(true));
        }

        PendingCode pendingCode = createPendingCode(accountEmail, CodeType.EMAIL_CHANGE);
        pendingCode.setCreatedBy(getCurrentUser());
        accountEmail.getPendingCodeList().add(pendingCode);
        accountEmail.setNewEmail(newEmail);
        accountEmail.setModifiedBy(getCurrentUser());

        accountFacade.edit(accountEmail);
        emailSender.sendEmailChange(accountEmail, pendingCode.getCode());
    }

    /**
     * Tworzy obiekt PendingCode o podanym typie.
     *
     * @param account  konto użytkownika, które ma być właścicielem kodu.
     * @param codeType typ kodu.
     * @return obiekt klasy PendingCode
     */
    private PendingCode createPendingCode(Account account, CodeType codeType) {
        PendingCode pendingCode = new PendingCode();
        pendingCode.setCode(UUID.randomUUID().toString());
        pendingCode.setUsed(false);
        pendingCode.setAccount(account);
        pendingCode.setCodeType(codeType);
        pendingCode.setCreatedBy(account);

        return pendingCode;
    }

    /**
     * Przypisuje nowy adres email do konta użytkownika, które jest właścicielem żetonu. (aktualizacja loginu
     * użytkownika)
     * Dezaktywuje żeton.
     * Czyści pole odpowiedzialne za przechowywanie adresu email na potrzeby procesu weryfikacji (pole Account
     * .newEmail).
     *
     * @param code żeton zmiany adresu email przypisanego do konta.
     * @throws AppBaseException proces zmiany adresu email przypisanego do konta zakończył się niepowodzeniem.
     */
    @PermitAll
    public void confirmEmail(String code) throws AppBaseException {
        PendingCode pendingCode = pendingCodeFacade.findByCode(code);
        if (pendingCode.getCodeType() != CodeType.EMAIL_CHANGE) {
            throw CodeException.codeInvalid();
        }
        if (pendingCode.isUsed()) {
            throw CodeException.codeUsed();
        }

        Account account = pendingCode.getAccount();
        account.setEmail(account.getNewEmail());
        account.setNewEmail(null);
        account.setEmailModificationDate(Date.from(Instant.now()));
        account.setEmailModificationBy(account);

        pendingCode.setUsed(true);
        pendingCode.setModifiedBy(account);
        accountFacade.edit(account);
    }

    /**
     * Wysyła email na konto użytkownika z poziomem administracyjnym po zalogowaniu.
     * Reprezentuje powiadomienie o zalogowaniu na konto administratora.
     *
     * @param address adres logiczny z jakiego nastąpiło logowanie na konto z administracyjnym poziomem dostępu.
     * @param adminLogin login konta administratora.
     * @throws AppBaseException proces wysyłania powiadomienia zakończył się niepowodzeniem.
     */
    @PermitAll
    public void sendAdminLoginInfo(String adminLogin, String address) throws AppBaseException {
        Account account = accountFacade.findByLogin(adminLogin);
        emailSender.sendAdminLogin(account, address);
    }

    /**
     * Aktualizuje motyw interfejsu dla użytkownika.
     *
     * @param login login zalogowanego użytkownika
     * @param themeColor kolor interfejsu preferowany przez użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("editOwnThemeSettings")
    public void changeThemeColor(String login, ThemeColor themeColor) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        if (account.getThemeColor().equals(themeColor)) {
            throw AccountException.themeAlreadySet();
        }
        account.setThemeColor(themeColor);
        account.setModifiedBy(account);
        accountFacade.edit(account);
    }

    /**
     * Edytuje język przypisany do konta użytkownika
     *
     * @param language nowy język, który ma być przypisany do konta
     * @param login login użytkownika
     * @throws AppBaseException proces zmiany języka zakończył się niepowodzeniem
     */
    @RolesAllowed("editOwnLanguage")
    public void changeAccountLanguage(String login, String language) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);
        account.setLanguage(language);
        account.setModifiedBy(account);
        accountFacade.edit(account);
    }
}
