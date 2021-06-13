package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Role;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.RoleManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.Set;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RoleEndpoint extends AbstractEndpoint implements RoleEndpointLocal {

    @Inject
    private RoleManager roleManager;

    @Inject
    private AccountManager accountManager;

    @Override
    @RolesAllowed("addAccessLevel")
    public void grantAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(login);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        if (!verifyIntegrity(rolesIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        roleManager.grantAccessLevel(login, accessLevel);
    }

    @Override
    @RolesAllowed("deleteAccessLevel")
    public void revokeAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        Account editAccount = accountManager.findByLogin(login);
        RolesDto rolesIntegrity = mapToRolesDto(editAccount);
        if (!verifyIntegrity(rolesIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        roleManager.revokeAccessLevel(login, accessLevel);
    }

    @Override
    @RolesAllowed("getOtherAccountInfo")
    public RolesDto getUserRole(String login) throws AppBaseException {
        return mapToRolesDto(accountManager.findByLogin(login));
    }

    @Override
    @RolesAllowed("getOwnAccountInfo")
    public RolesDto getUserRole() throws AppBaseException {
        return mapToRolesDto(accountManager.findByLogin(getLogin()));
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

    @Override
    @RolesAllowed("getManagerData")
    public ManagerDataDto getManagerData(String login) throws AppBaseException {
        Account account = accountManager.findByLogin(login);
        Set<Role> roleList = account.getRoleList();
        Role managerRole = null;
        for (Role role: roleList) {
            if (role.getAccessLevel() == AccessLevel.MANAGER && role.isEnabled()) {
                managerRole = role;
            }
        }
        ManagerData managerData = roleManager.find(managerRole.getId());

        return Mappers.getMapper(IRoleMapper.class).toManagerDataDto(managerData);
    }
}
