package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.ManagerDataFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Odpowiada za odnajdywanie ról managerów w systemie.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RoleManager {

    @Inject
    private ManagerDataFacade managerDataFacade;

    /**
     * Wyszukuje obiekt ManagerData o podanym identyfikatorze.
     *
     * @param id identyfikator wyszukiwanej roli managera.
     * @return wyszukiwana rola managera
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @PermitAll
    public ManagerData find(Long id) throws AppBaseException {
        return managerDataFacade.find(id);
    }
}
