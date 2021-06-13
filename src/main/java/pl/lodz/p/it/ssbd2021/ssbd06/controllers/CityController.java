package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.CityEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie miastami.
 */
@Path("/cities/")
public class CityController extends AbstractController {

    @Inject
    private CityEndpointLocal cityEndpointLocal;

    @Inject
    private MessageSigner messageSigner;

    /**
     * Zwraca miasto o podanym identyfikatorze
     *
     * @param id identyfikator miasta
     * @throws AppBaseException podczas błędu związanego z pobieraniem miasta
     * @return dto miasta
     */
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) throws AppBaseException {
        CityDto cityDto = cityEndpointLocal.get(id);
        return Response.ok()
                .entity(cityDto)
                .header("ETag", messageSigner.sign(cityDto))
                .build();
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
    @EtagValidatorFilterBinding
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateCity(@NotNull @Valid CityDto cityDto) throws AppBaseException {
        repeat(() -> cityEndpointLocal.updateCity(cityDto), cityEndpointLocal);
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
