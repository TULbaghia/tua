package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewCityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.validation.Valid;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie miastami.
 */
@Local
public interface CityEndpointLocal extends CallingClass {
    /**
     * Zwraca miasto o podanym identyfikatorze
     *
     * @param id identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return dto miasta
     */
    CityDto get(Long id) throws AppBaseException;

    /**
     * Zwraca listę miast
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista miast
     */
    @RolesAllowed("getAllCities")
    List<CityDto> getAllCities() throws AppBaseException;

    /**
     * Dodaje miasto
     *
     * @param newCityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addCity")
    void addCity(NewCityDto newCityDto) throws AppBaseException;

    /**
     * Modyfikuje miasto
     *
     * @param cityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateCity")
    void updateCity(CityDto cityDto) throws AppBaseException;

    /**
     * Usuwa miasto
     *
     * @param cityId identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteCity")
    void deleteCity(Long cityId) throws AppBaseException;
}
