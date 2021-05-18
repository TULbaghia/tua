package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.RoleManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RoleEndpoint extends AbstractEndpoint implements RoleEndpointLocal {

    @Inject
    private RoleManager roleManager;

    @Inject
    private AccountManager accountManager;

    @Override
    @RolesAllowed("addAccessLevel")
    public void grantAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        if (!verifyIntegrity(mapToRolesDto(accountManager.getAccountByLogin(login)))) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        roleManager.grantAccessLevel(login, accessLevel);
    }

    @Override
    @RolesAllowed("deleteAccessLevel")
    public void revokeAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        if (!verifyIntegrity(mapToRolesDto(accountManager.getAccountByLogin(login)))) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        roleManager.revokeAccessLevel(login, accessLevel);
    }

    @Override
    @RolesAllowed("getOtherAccountInfo")
    public RolesDto getUserRole(String login) throws AppBaseException {
        return mapToRolesDto(accountManager.getAccountByLogin(login));
    }

    @Override
    @RolesAllowed("getOwnAccountInfo")
    public RolesDto getUserRole() throws AppBaseException {
        return mapToRolesDto(accountManager.getAccountByLogin(getLogin()));
    }

    /**
     * Mapuje obiekt encji użytkownika do listy ról
     *
     * @param account encja użytkownika
     * @return dto zawierające poziomy dostępu przypisane danemu użytkownikowi
     */
    private RolesDto mapToRolesDto(Account account) {
        return Mappers.getMapper(IRoleMapper.class).toRolesDto(account);
    }

}
