package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewCityDto;
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
    private CityEndpointLocal cityEndpoint;

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
        CityDto cityDto = cityEndpoint.get(id);
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
    @Operation(operationId = "getAllCities", summary = "getAllCities")
    public List<CityDto> getAll() throws AppBaseException {
        return repeat(() -> cityEndpoint.getAllCities(), cityEndpoint);
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
        repeat(() -> cityEndpoint.addCity(newCityDto), cityEndpoint);
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
        repeat(() -> cityEndpoint.updateCity(cityDto), cityEndpoint);
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
