package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface RoleEndpointLocal {

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param userId      identyfikator użytkownika
     * @param accessLevel poziom dostępu do przyznania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("addAccessLevel")
    void grantAccessLevel(Long userId, AccessLevel accessLevel) throws AppBaseException;

}
