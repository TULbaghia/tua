package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountPersonalDetailsDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.PasswordChangeDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.PasswordChangeOtherDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.PasswordResetDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.security.PasswordHasher;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.PendingCodeManager;
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
import java.util.Date;
import java.util.List;


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
        Account account = Mappers.getMapper(IAccountMapper.class).toAccount(registerAccountDto);
        account.setLanguage(servletRequest.getLocale().getLanguage());
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
        processEditDetails(getLogin(), accountPersonalDetailsDto);
    }

    @Override
    @RolesAllowed("editOtherAccountDetails")
    public void editOtherAccountDetails(String login, AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        processEditDetails(login, accountPersonalDetailsDto);
    }

    private void processEditDetails(String login, AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
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
        return accountManager.getAllAccounts();
    }

    /**
     * Zwraca dane konkretnego użytkownika
     *
     * @param login login użytkownika
     * @return dane konta wybranego użytkownika
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOtherAccountInfo")
    public AccountDto getAccount(String login) throws AppBaseException {
        return accountManager.getAccount(login);
    }
    /**
     * Zwraca dane konta użytkownika, który wygenerował żądanie
     *
     * @return dane konta
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOwnAccountInfo")
    public AccountDto getOwnAccountInfo() throws AppBaseException {
        return accountManager.getAccount(super.getLogin());
    }

    @Override
    @RolesAllowed("editOwnPassword")
    public void changePassword(PasswordChangeDto passwordChangeDto) throws AppBaseException {
        Account account = accountManager.getCurrentUser();
        AccountDto accountDto = Mappers.getMapper(IAccountMapper.class).toAccountDto(account);
        if(!verifyIntegrity(accountDto)) throw AppOptimisticLockException.optimisticLockException();
        if(!PasswordHasher.check(passwordChangeDto.getOldPassword(), account.getPassword())) throw AccountException.passwordsDontMatch();

        accountManager.changePassword(account, passwordChangeDto.getNewPassword());
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
    public void sendResetPassword(String login) throws AppBaseException {
        pendingCodeManager.sendResetPassword(login);
    }

    @Override
    @PermitAll
    public void sendResetPasswordAgain(String login) throws AppBaseException {
        pendingCodeManager.sendResetPasswordAgain(login);
    }

    @Override
    @RolesAllowed("editOtherPassword")
    public void changeOtherPassword(PasswordChangeOtherDto passwordChangeOtherDto) throws AppBaseException {
        pendingCodeManager.sendResetPassword(passwordChangeOtherDto.getLogin());
        Account account = accountManager.findByLogin(passwordChangeOtherDto.getLogin());
        accountManager.changePassword(account, passwordChangeOtherDto.getGivenPassword());
    }
}
