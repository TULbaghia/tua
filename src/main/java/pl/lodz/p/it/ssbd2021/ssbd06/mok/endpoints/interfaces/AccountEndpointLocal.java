package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.ejb.Local;

@Local
public interface AccountEndpointLocal {


    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     */
    void confirmAccount(String code) throws AppBaseException;
}
