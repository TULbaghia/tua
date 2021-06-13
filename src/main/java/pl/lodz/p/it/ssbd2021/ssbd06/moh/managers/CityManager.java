package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.CityFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.CityFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.HotelFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manager odpowiadający za zarządzanie miastami.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CityManager {

    @Inject
    private CityFacade cityFacade;

    /**
     * Zwraca miasto o podanym identyfikatorze
     *
     * @param id identyfikator miasta.
     * @throws AppBaseException gdy nie udało się pobrać danych lub podczas błędu z bazą danych
     * @return wyszukiwane miasto.
     */
    @PermitAll
    public City get(Long id) throws AppBaseException {
        return Optional.ofNullable(cityFacade.find(id)).orElseThrow(NotFoundException::cityNotFound);
    }

    /**
     * Zwraca listę miast
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista miast
     */
    @RolesAllowed("getAllCities")
    public List<City> getAll() throws AppBaseException {
        return new ArrayList<>(cityFacade.findAll());
    }

    /**
     * Dodaje miasto
     *
     * @param cityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addCity")
    void addCity(CityDto cityDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje miasto
     *
     * @param cityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateCity")
    void updateCity(CityDto cityDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa miasto
     *
     * @param cityId identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteCity")
    void deleteCity(Long cityId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyszukuje obiekt City o podanej nazwie.
     *
     * @param name nazwa miasta.
     * @return wyszukiwane miasto.
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @PermitAll
    public City findByName(String name) throws AppBaseException {
        return cityFacade.findByName(name);
    }
}
