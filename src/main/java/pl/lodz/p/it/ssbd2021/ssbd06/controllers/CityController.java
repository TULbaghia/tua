package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewCityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.CityEndpointLocal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie miastami.
 */
@Path("/cities/")
public class CityController extends AbstractController {

    @Inject
    private CityEndpointLocal cityEndpointLocal;

    /**
     * Zwraca miasto o podanym identyfikatorze
     *
     * @param id identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z pobieraniem miasta
     * @return dto miasta
     */
    @GET
    @Path("/{id}")
    public CityDto get(@PathParam("id") Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę miast
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy miast
     * @return lista dto miast
     */
    @GET
    @RolesAllowed("getAllCities")
    public List<CityDto> getAllCities() throws AppBaseException {
        return cityEndpointLocal.getAllCities();
    }

    /**
     * Dodaje nowe miasto.
     *
     * @param newCityDto dto z danymi miasta.
     * @throws AppBaseException podczas błędu związanego z dodawaniem miasta.
     */
    @POST
    @RolesAllowed("addCity")
    public void addCity(@Valid NewCityDto newCityDto) throws AppBaseException {
        repeat(() -> cityEndpointLocal.addCity(newCityDto), cityEndpointLocal);
    }

    /**
     * Modyfikuje miasto
     *
     * @param cityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z aktualizacją miasta
     */
    @PUT
    @RolesAllowed("updateCity")
    public void updateCity(CityDto cityDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa miasto
     *
     * @param cityId identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z usuwaniem miasta
     */
    @DELETE
    @RolesAllowed("deleteCity")
    @Path("/{id}")
    public void deleteCity(@PathParam("id") Long cityId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
