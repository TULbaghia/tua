package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BoxEndpointLocal;
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
     * Zwraca listę klatek
     *
     * @return lista dto klatek
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     */
    @GET
    @RolesAllowed("getAllBoxes")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllBoxesList", summary = "getAllBoxesList")
    public List<BoxDto> getAll() throws AppBaseException {
        return repeat(() -> boxEndpoint.getAll(), boxEndpoint);
    }

    /**
     * Zwraca listę klatek przypisanych do hotelu
     *
     * @param loginManger login menadżera hotelu
     * @return lista dto klatek przypisanych do hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
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
     * Zwraca listę klatek przypisanych do hotelu po id
     *
     * @param id identyfikator hotelu
     * @return lista dto klatek przypisanych do hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     */
    @GET
    @Path("/all/id/{id}")
    @RolesAllowed("getAllBoxes")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllBoxesInHotelById", summary = "getAllBoxesInHotelById")
    public List<BoxDto> getAllBoxesInHotelById(@NotNull @PathParam("id") Long id) throws AppBaseException {
        return repeat(() -> boxEndpoint.getAllBoxesInHotel(id), boxEndpoint);
    }

    /**
     * Zwraca listę klatek przypisanych do hotelu przeznaczonych dla konkretnego typu zwierzęcia
     *
     * @param id         identyfikator hotelu
     * @param animalType typ zwierzęcia
     * @return lista dto klatek przypisanych do hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     */
    @GET
    @Path("/all/{id}/{animalType}")
    @RolesAllowed("getAllBoxes")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getSomeTypeBoxesFromHotel", summary = "getSomeTypeBoxesFromHotel")
    public List<BoxDto> getSomeTypeBoxesFromHotel(@NotNull @PathParam("id") Long id,
                                                  @NotNull @PathParam("animalType") AnimalType animalType)
            throws AppBaseException {
        return repeat(() -> boxEndpoint.getSomeTypeBoxesFromHotel(id, animalType), boxEndpoint);
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
}
