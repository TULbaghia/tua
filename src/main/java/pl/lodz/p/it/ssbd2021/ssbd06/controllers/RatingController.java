package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.RatingEndpointLocal;
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
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie ocenami hoteli.
 */
@Path("/ratings")
public class RatingController extends AbstractController {

    @Inject
    private RatingEndpointLocal ratingEndpoint;

    @Inject
    private MessageSigner messageSigner;

    /**
     * Zwraca ocenę hotelu
     *
     * @param id identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return obiekt dto oceny hotelu
     */
    @GET
    @Path("/get/{id}")
    @PermitAll
    @Operation(operationId = "getRating", summary = "getRating")
    public Response get(@NotNull @PathParam("id") Long id) throws AppBaseException {
        RatingDto ratingDto = repeat(() -> ratingEndpoint.get(id), ratingEndpoint);
        return Response.ok()
                .entity(ratingDto)
                .header("ETag", messageSigner.sign(ratingDto))
                .build();
    }

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego ze zwracaniem listy ocen hotelu
     * @return lista ocen hotelu
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllRatingsList", summary = "getAllRatingsList")
    public List<RatingDto> getAll(@PathParam("id") Long hotelId) throws AppBaseException {
        return repeat(() -> ratingEndpoint.getAll(hotelId), ratingEndpoint);
    }

    /**
     * Zwraca ocenę o podanym id
     * @param ratingId id oceny
     * @return ocena
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @RolesAllowed("getHotelRating")
    @Path("/rating/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getHotelRating", summary = "getHotelRating")
    public Response getRating(@PathParam("id") Long ratingId) throws AppBaseException {
        RatingDto ratingDto = repeat(() -> ratingEndpoint.getRating(ratingId), ratingEndpoint);
        return Response.ok()
                .entity(ratingDto)
                .header("ETag", messageSigner.sign(ratingDto))
                .build();
    }

    /**
     * Dodaje ocene
     *
     * @param newRatingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z dodawaniem oceny hotelu
     */
    @POST
    @RolesAllowed("addHotelRating")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "addRating", summary = "addRating")
    public void addRating(@NotNull @Valid NewRatingDto newRatingDto) throws AppBaseException {
        repeat(() -> ratingEndpoint.addRating(newRatingDto), ratingEndpoint);
    }

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z aktualizacją oceny hotelu
     */
    @PUT
    @RolesAllowed("updateHotelRating")
    @Operation(operationId = "updateRating", summary = "updateRating")
    public void updateRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa ocenę hotelu
     *
     * @param ratingId identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z usunięciem oceny hotelu
     */
    @DELETE
    @RolesAllowed("deleteHotelRating")
    @Path("/{id}")
    @EtagValidatorFilterBinding
    @Operation(operationId = "deleteRating", summary = "deleteRating")
    public void deleteRating(@NotNull @PathParam("id") Long ratingId) throws AppBaseException {
        repeat(() -> ratingEndpoint.deleteRating(ratingId), ratingEndpoint);
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId id oceny hotelu
     * @throws AppBaseException podczas błędu związanego ze zmianą widoczności oceny
     */
    @PATCH
    @RolesAllowed("hideHotelRating")
    @Path("/changeVisibility/{ratingId}")
    @Operation(operationId = "changeVisibility", summary = "changeVisibility")
    public void changeVisibility(@PathParam("ratingId") Long ratingId) throws AppBaseException {
        repeat(() -> ratingEndpoint.changeVisibility(ratingId), ratingEndpoint);
    }
}
