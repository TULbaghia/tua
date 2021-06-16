package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BoxFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manager odpowiadający za zarządzanie klatkami.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BoxManager {

    @Inject
    private BoxFacade boxFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private HotelManager hotelManager;

    @Inject
    private HttpServletRequest servletRequest;

    /**
     * Zwraca klatke o podanym identyfikatorze
     *
     * @param id identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return encja klatki
     */
    public Box get(Long id) throws AppBaseException {
        return Optional.ofNullable(boxFacade.find(id)).orElseThrow(NotFoundException::boxNotFound);
    }

    /**
     * Zwraca listę klatek
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista klatek
     */
    @RolesAllowed("getAllBoxes")
    public List<Box> getAll() throws AppBaseException {
        return new ArrayList<>(boxFacade.findAll());
    }

    /**
     * Dodaje nową klatkę
     *
     * @param box dodawana nowa klatka
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addBox")
    public void addBox(Box box) throws AppBaseException {
        box.setHotel(hotelManager.findHotelByManagerLogin(getLogin()));
        box.setCreatedBy(accountFacade.findByLogin(getLogin()));
        boxFacade.create(box);
    }

    /**
     * Modyfikuje klatkę
     *
     * @param box encja z danymi klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateBox")
    public void updateBox(Box box) throws AppBaseException {
        box.setModifiedBy(accountFacade.findByLogin(getLogin()));
        boxFacade.edit(box);
    }

    /**
     * Usuwa klatkę
     *
     * @param boxId identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteBox")
    void deleteBox(Long boxId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera z obiektu żądania login użytkownika
     *
     * @return login uzytkownika wywołującego żądanie
     */
    private String getLogin() {
        return servletRequest.getUserPrincipal().getName();
    }
}
