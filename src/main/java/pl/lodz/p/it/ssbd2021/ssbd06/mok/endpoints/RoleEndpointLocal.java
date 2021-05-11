package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface RoleEndpointLocal {

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param userId      identyfikator użytkownika
     * @param accessLevel poziom dostępu do odebrania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("deleteAccessLevel")
    void revokeAccessLevel(Long userId, AccessLevel accessLevel) throws AppBaseException;
}
