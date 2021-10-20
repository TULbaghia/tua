package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacadeMoh;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Odpowiada za odnajdywanie kont w systemie.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountManagerMoh {

    @Inject
    private AccountFacadeMoh accountFacadeMoh;

    /**
     * Wyszukuje obiekt Acccount o podanym loginie.
     *
     * @param login login wyszukiwanego konta użytkownika.
     * @return konto wyszukiwanego użytkownika
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @RolesAllowed("addManagerToHotel")
    public Account findByLogin(String login) throws AppBaseException {
        return accountFacadeMoh.findByLogin(login);
    }
}
