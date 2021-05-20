package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RoleException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RoleManager {

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private EmailSender emailSender;


    /**
     * Odbiera użytkownikowi poziom dostępu.
     *
     * @param login       login użytkownika
     * @param accessLevel poziom dostępu
     * @throws AppBaseException gdy nie udało się odebrać poziomu dostępu
     */
    @RolesAllowed("deleteAccessLevel")
    public void revokeAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);

        if (account == null) {
            throw NotFoundException.accountNotFound();
        }

        Role role = account.getRoleList().stream()
                .filter(x -> accessLevel.equals(x.getAccessLevel()))
                .findFirst()
                .orElse(null);

        if (role == null || !role.isEnabled()) {
            throw RoleException.alreadyRevoked();
        }
        role.setModifiedBy(accountFacade.findByLogin(servletRequest.getUserPrincipal().getName()));
        role.setEnabled(false);
        accountFacade.edit(account);
        emailSender.sendDenyAccessLevelEmail(account, accessLevel.toString());
    }

    /**
     * Przyznaje użytkownikowi poziom dostępu.
     *
     * @param login       login użytkownika
     * @param accessLevel poziom dostępu
     * @throws AppBaseException gdy nie udało się przyznać poziomu dostępu
     */
    @RolesAllowed("addAccessLevel")
    public void grantAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException {
        Account account = accountFacade.findByLogin(login);

        if (account == null) {
            throw NotFoundException.accountNotFound();
        }

        Role role = account.getRoleList().stream()
                .filter(x -> accessLevel.equals(x.getAccessLevel()))
                .findFirst()
                .orElse(null);

        if (role != null) {
            if (role.isEnabled()) {
                throw RoleException.alreadyGranted();
            } else {
                role.setModifiedBy(accountFacade.findByLogin(servletRequest.getUserPrincipal().getName()));
            }
        } else {
            role = createUserRole(accessLevel);
            role.setAccount(account);
            role.setCreatedBy(accountFacade.findByLogin(servletRequest.getUserPrincipal().getName()));
            account.getRoleList().add(role);
        }
        role.setEnabled(true);
        accountFacade.edit(account);
        emailSender.sendGrantAccessLevelEmail(account, accessLevel.toString());
    }

    /**
     * Tworzy obiekt roli użytkownika w zależności od poziomu dostępu
     *
     * @param accessLevel poziom dostępu
     * @return obiekt roli uzytkownika
     * @throws RoleException gdy poziom dostępu nie istnieje
     */
    private Role createUserRole(AccessLevel accessLevel) throws RoleException {
        switch (accessLevel) {
            case ADMIN:
                return new AdminData();
            case MANAGER:
                return new ManagerData();
            case CLIENT:
                return new ClientData();
        }
        throw RoleException.unsupportedAccessLevel();
    }

}
