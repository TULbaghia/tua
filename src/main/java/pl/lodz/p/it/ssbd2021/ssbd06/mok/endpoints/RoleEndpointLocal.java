package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface RoleEndpointLocal {

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param login      identyfikator użytkownika
     * @param accessLevel poziom dostępu do przyznania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("addAccessLevel")
    void grantAccessLevel(String login, AccessLevel accessLevel) throws AppBaseException;

    /**
     * Zwraca zachowawcze role użytkownika
     *
     * @param login identyfikator użytkownika
     * @return dto zawierające poziomy dostępu przypisane danemu użytkownikowi
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @RolesAllowed("getOtherAccountInfo")
    RolesDto getUserRole(String login) throws AppBaseException;

    /**
     * Zwraca zachowawcze role zalogowanego użytkownika
     *
     * @return dto zawierające poziomy dostępu przypisane danemu użytkownikowi
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @RolesAllowed("getOwnAccountInfo")
    RolesDto getUserRole() throws AppBaseException;
}
