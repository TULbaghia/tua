package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
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

    @Override
    @RolesAllowed("deleteAccessLevel")
    public void revokeAccessLevel(Long userId, AccessLevel accessLevel) throws AppBaseException {
        roleManager.revokeAccessLevel(userId, accessLevel);
    }

}
