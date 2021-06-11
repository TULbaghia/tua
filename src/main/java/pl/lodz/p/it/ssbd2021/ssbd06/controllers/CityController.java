package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.CityEndpointLocal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie miastami.
 */
@Path("/cities/")
public class CityController extends AbstractController {

    @Inject
    private CityEndpointLocal cityEndpoint;

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
    @Operation(operationId = "getAllCities", summary = "getAllCities")
    public List<CityDto> getAll() throws AppBaseException {
        return repeat(() -> cityEndpoint.getAll(), cityEndpoint);
    }

    /**
     * Dodaje miasto
     *
     * @param cityDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z dodawaniem miasta
     */
    @POST
    @RolesAllowed("addCity")
    public void addCity(CityDto cityDto) throws AppBaseException {
        throw new UnsupportedOperationException();
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
