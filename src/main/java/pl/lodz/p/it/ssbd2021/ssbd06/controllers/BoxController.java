package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.DateParam;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BoxEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie klatkami.
 */
@Path("/boxes")
public class BoxController extends AbstractController {

    @Inject
    private BoxEndpointLocal boxEndpoint;

    @Inject
    MessageSigner messageSigner;

    /**
     * Zwraca klatkę o podanym identyfikatorze
     *
     * @param id identyfikator klatki
     * @return dto klatki
     * @throws AppBaseException podczas błędu związanego z pobieraniem klatki
     */
    @GET
    @Path("/{id}")
    @Operation(operationId = "getBox", summary = "getBox")
    public Response get(@PathParam("id") Long id) throws AppBaseException {
        BoxDto boxDto = boxEndpoint.get(id);
        return Response.ok()
                .entity(boxDto)
                .header("ETag", messageSigner.sign(boxDto))
                .build();
    }

    /**
     * Zwraca listę klatek przypisanych do hotelu
     *
     * @param loginManger login menadżera hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     * @return lista dto klatek przypisanych do hotelu
     */
    @GET
    @Path("/all/{login}")
    @RolesAllowed("getAllBoxes")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllBoxesInHotelByLogin", summary = "getAllBoxesInHotelByLogin")
    public List<BoxDto> getAllBoxesInHotel(@NotNull @PathParam("login") String loginManger) throws AppBaseException {
        return repeat(() -> boxEndpoint.getAllBoxesInHotel(loginManger), boxEndpoint);
    }

    /**
     * Zwraca listę klatek przypisanych do hotelu
     *
     * @param id identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     * @return lista dto klatek przypisanych do hotelu
     */
    @GET
    @Path("/all/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllBoxesInHotelById", summary = "getAllBoxesInHotelById")
    public List<BoxDto> getAllBoxesInHotelById(@NotNull @PathParam("id") Long id) throws AppBaseException {
        return repeat(() -> boxEndpoint.getAllBoxesInHotel(id), boxEndpoint);
    }

    /**
     * Dodaje nową klatkę
     *
     * @param boxDto dto z danymi nowej klatki
     * @throws AppBaseException podczas błędu związanego z dodaniem klatki
     */
    @POST
    @RolesAllowed("addBox")
    @Operation(operationId = "addBox", summary = "addBox")
    public void addBox(@NotNull @Valid NewBoxDto boxDto) throws AppBaseException {
        repeat(() -> boxEndpoint.addBox(boxDto), boxEndpoint);
    }

    /**
     * Modyfikuje klatkę
     *
     * @param boxDto dto z danymi klatki
     * @throws AppBaseException podczas błędu związanego z aktualizacją klatki
     */
    @PUT
    @RolesAllowed("updateBox")
    @EtagValidatorFilterBinding
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateBox", summary = "updateBox")
    public void updateBox(@NotNull @Valid UpdateBoxDto boxDto) throws AppBaseException {
        repeat(() -> boxEndpoint.updateBox(boxDto), boxEndpoint);
    }

    /**
     * Usuwa klatkę
     *
     * @param boxId identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z usunięciem klatki
     */
    @DELETE
    @RolesAllowed("deleteBox")
    @Path("/{id}")
    @Operation(operationId = "deleteBox", summary = "deleteBox")
    public void deleteBox(@PathParam("id") Long boxId) throws AppBaseException {
        repeat(() -> boxEndpoint.deleteBox(boxId), boxEndpoint);
    }


    /**
     * Pobiera klatki z zadanego hotelu nie zajęte przez żadną rezerwacje w zadanym zakresie czasu
     *
     * @param hotelId identyfikator hotelu
     * @param dateFrom początek zakresu czasu kiedy klatka jest wolna
     * @param dateTo koniec zakresu czasu kiedy klatka jest wolna
     * @return listę wolnych klatek w danym hotelu w zadanym zakresie czasu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     */
    @GET
    @RolesAllowed("getAllBoxes")
    @Path("/all/{id}/from/{date_from}/to/{date_to}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAvailableBoxesBetween", summary = "getAvailableBoxesBetween")
    public List<BoxDto> getAvailableBoxesBetween(@PathParam("id") Long hotelId, @PathParam("date_from") DateParam dateFrom, @PathParam("date_to") DateParam dateTo) throws AppBaseException {
        return repeat(() -> boxEndpoint.getAvailableBoxesBetween(hotelId, dateFrom.getDate(), dateTo.getDate()), boxEndpoint);
    }
}
