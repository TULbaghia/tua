package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.PasswordChangeOtherDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.PasswordResetDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.PendingCodeManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Stateful
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
