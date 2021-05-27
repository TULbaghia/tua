package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie ocenami hoteli.
 */
@Path("/ratings")
public class RatingController extends AbstractController {

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego ze zwracaniem listy ocen hotelu
     * @return lista ocen hotelu
     */
    @GET
    @Path("/{id}")
    public List<RatingDto> getAll(@PathParam("id") Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje ocene
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z dodawaniem oceny hotelu
     */
    @POST
    @RolesAllowed("addHotelRating")
    public void addRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z aktualizacją oceny hotelu
     */
    @PUT
    @RolesAllowed("updateHotelRating")
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
    public void deleteRating(@PathParam("id") Long ratingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId id oceny hotelu
     * @param ratingVisibility poziom widoczności
     * @throws AppBaseException podczas błędu związanego ze zmianą widoczności oceny
     */
    @PATCH
    @RolesAllowed("hideHotelRating")
    @Path("/{ratingId}/{visibility}")
    public void changeVisibility(@PathParam("ratingId") Long ratingId, @PathParam("visibility") RatingVisibility ratingVisibility) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
