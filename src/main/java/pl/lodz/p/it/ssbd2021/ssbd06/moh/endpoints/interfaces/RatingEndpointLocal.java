package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie ocenami hoteli.
 */
@Local
public interface RatingEndpointLocal extends CallingClass {

    /**
     * Zwraca ocenę hotelu
     *
     * @param id identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return obiekt oceny hotelu
     */
    @PermitAll
    RatingDto get(Long id) throws AppBaseException;

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista ocen hotelu
     */
    @PermitAll
    List<RatingDto> getAll(Long hotelId) throws AppBaseException;

    /**
     * Dodaje ocene
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotelRating")
    void addRating(NewRatingDto ratingDto) throws AppBaseException;

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateHotelRating")
    void updateRating(RatingDto ratingDto) throws AppBaseException;

    /**
     * Usuwa ocenę hotelu
     *
     * @param ratingId identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteHotelRating")
    void deleteRating(Long ratingId) throws AppBaseException;

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId dto z danymi hotelu
     * @param ratingVisibility poziom widoczności
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("hideHotelRating")
    void changeVisibility(Long ratingId, RatingVisibility ratingVisibility) throws AppBaseException;
}
