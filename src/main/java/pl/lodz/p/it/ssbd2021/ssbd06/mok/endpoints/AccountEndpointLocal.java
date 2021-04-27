package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;


import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

@Local
public interface AccountEndpointLocal {
    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @RolesAllowed("blockAccount")
    void blockAccount(String login) throws AppBaseException;
}
