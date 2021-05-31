package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.ThemeColor;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.PendingCodeManager;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Log
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint extends AbstractEndpoint implements AccountEndpointLocal {

    @Inject
    private AccountManager accountManager;

    @Inject
    private PendingCodeManager pendingCodeManager;

    @Inject
    private HttpServletRequest servletRequest;

    @Override
    @RolesAllowed("blockAccount")
    public void blockAccount(String login) throws AppBaseException {
        accountManager.blockAccount(login);
    }

    @Override
    @RolesAllowed("unblockAccount")
    public void unblockAccount(String login) throws AppBaseException {
        accountManager.unblockAccount(login);
    }

    @Override
    @PermitAll
    public void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException {
        Account account = new Account();
        Mappers.getMapper(IAccountMapper.class).toAccount(registerAccountDto, account);
        account.setLanguage(servletRequest.getLocale().getLanguage().toLowerCase());
        accountManager.register(account);
    }

    @Override
    @PermitAll
    public void confirmAccount(String code) throws AppBaseException {
        accountManager.confirm(code);
    }

    @Override
    @PermitAll
    public void updateValidAuth(String login, String ipAddress, Date authDate) throws AppBaseException {
        accountManager.updateValidAuth(login, ipAddress, authDate);
    }

    @Override
    @PermitAll
    public void updateInvalidAuth(String login, String ipAddress, Date authDate) throws AppBaseException {
        accountManager.updateInvalidAuth(login, ipAddress, authDate);
    }

    @Override
    @RolesAllowed("editOwnAccountDetails")
    public void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        Mappers.getMapper(IAccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountManager.editAccountDetails(editAccount);
    }

    @Override
    @RolesAllowed("editOtherAccountDetails")
    public void editOtherAccountDetails(String login, AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        Account editAccount = accountManager.findByLogin(login);
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        Mappers.getMapper(IAccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountManager.editAccountDetails(editAccount);
    }

    @Override
    @RolesAllowed("getAllAccounts")
    public List<AccountDto> getAllAccounts() throws AppBaseException {
        List<Account> accounts = accountManager.getAllAccounts();
        List<AccountDto> result = new ArrayList<>();
        for (Account account: accounts){
            result.add(Mappers.getMapper(IAccountMapper.class).toAccountDto(account));
        }
        return result;
    }

    @Override
    @RolesAllowed("getOtherAccountInfo")
    public AccountDto getAccount(String login) throws AppBaseException {
        return Mappers.getMapper(IAccountMapper.class).toAccountDto(accountManager.getAccount(login));
    }

    @Override
    @RolesAllowed("getOwnAccountInfo")
    public AccountDto getOwnAccountInfo() throws AppBaseException {
        return Mappers.getMapper(IAccountMapper.class).toAccountDto(accountManager.getAccount(getLogin()));
    }

    @Override
    @RolesAllowed("editOwnPassword")
    public void changePassword(PasswordChangeDto passwordChangeDto) throws AppBaseException {
        Account editAccount = accountManager.getCurrentUser();
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        if (!PasswordHasher.check(passwordChangeDto.getOldPassword(), editAccount.getPassword())) {
            throw AccountException.passwordsDontMatch();
        }
        accountManager.changePassword(editAccount, passwordChangeDto.getNewPassword());
    }

    @Override
    @PermitAll
    public void resetPassword(PasswordResetDto passwordResetDto) throws AppBaseException {
        String password = passwordResetDto.getPassword();
        String code = passwordResetDto.getResetCode();
        accountManager.resetPassword(password, code);
    }

    @Override
    @PermitAll
    public void sendResetPassword(String email) throws AppBaseException {
        pendingCodeManager.sendResetPassword(email);
    }

    @Override
    @PermitAll
    public void sendResetPasswordAgain(String email) throws AppBaseException {
        pendingCodeManager.sendResetPasswordAgain(email);
    }

    @Override
    @RolesAllowed("editOtherPassword")
    public void changeOtherPassword(PasswordChangeOtherDto passwordChangeOtherDto) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(passwordChangeOtherDto.getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        pendingCodeManager.sendResetPassword(editAccount.getEmail());
        accountManager.changePassword(editAccount, passwordChangeOtherDto.getGivenPassword());
    }

    @Override
    @RolesAllowed("editOwnAccountEmail")
    public void editOwnAccountEmail(EmailDto emailDto) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountManager.editAccountEmail(getLogin(), emailDto.getNewEmail());
    }

    @Override
    @RolesAllowed("editOtherAccountEmail")
    public void editOtherAccountEmail(EmailDto emailDto, String login) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(login);
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountManager.editAccountEmail(login, emailDto.getNewEmail());
    }

    @Override
    @PermitAll
    public void confirmEmail(String code) throws AppBaseException {
        accountManager.confirmEmail(code);
    }

    @Override
    @RolesAllowed("changeAccessLevel")
    public void changeOwnAccessLevel(AccessLevel accessLevel) {
        log.log(Level.INFO, "User {0} change access level on: {1}", new Object[]{getLogin(), accessLevel.toString()});
    }

    /**
     * Wysyła email na konto użytkownika z poziomem administracyjnym po zalogowaniu.
     * Reprezentuje powiadomienie o zalogowaniu na konto administratora.
     *
     * @param address    adres logiczny z jakiego nastąpiło logowanie na konto z administracyjnym poziomem dostępu.
     * @param adminLogin login konta administratora.
     * @throws AppBaseException proces wysyłania powiadomienia zakończył się niepowodzeniem.
     */
    @Override
    @PermitAll
    public void sendAdminLoginInfo(String adminLogin, String address) throws AppBaseException {
        accountManager.sendAdminLoginInfo(adminLogin, address);
    }

    @Override
    @RolesAllowed("editOwnLanguage")
    public void editOwnLanguage(String language) throws AppBaseException {
        Account account = accountManager.findByLogin(getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(account);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountManager.changeAccountLanguage(getLogin(), language);
    }

    /**
     * Aktualizuje motyw interfejsu dla użytkownika.
     *
     * @param themeColor kolor interfejsu preferowany przez użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @Override
    @RolesAllowed("editOwnThemeSettings")
    public void changeThemeColor(ThemeColor themeColor) throws AppBaseException {
        Account account = accountManager.findByLogin(getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(account);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountManager.changeThemeColor(getLogin(), themeColor);
    }
}
