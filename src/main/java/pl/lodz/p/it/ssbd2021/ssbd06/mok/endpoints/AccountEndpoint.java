package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountPersonalDetailsDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import javax.security.enterprise.SecurityContext;

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
    private HttpServletRequest servletRequest;

    @Inject
    private SecurityContext securityContext;

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
    @RolesAllowed("editOwnAccountDetails")
    public void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        String authUser = securityContext.getCallerPrincipal().getName();
        accountPersonalDetailsDto.setLogin(authUser);
        processEditDetails(accountPersonalDetailsDto);
    }

    @RolesAllowed("editOtherAccountDetails")
    public void editOtherAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        processEditDetails(accountPersonalDetailsDto);
    }

    private void processEditDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(accountPersonalDetailsDto.getLogin());
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        Mappers.getMapper(IAccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountManager.editAccountDetails(editAccount);
    }
}
