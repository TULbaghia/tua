package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateRatingDto;
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
     * @return obiekt oceny hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"getHotelRating"})
    RatingDto get(Long id) throws AppBaseException;

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @return lista ocen hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    List<RatingDto> getAll(Long hotelId) throws AppBaseException;

    /**
     * Zwraca ocenę o podanym id
     *
     * @param ratingId id oceny
     * @return ocena
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("getHotelRating")
    RatingDto getRating(Long ratingId) throws AppBaseException;

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
     * @param updateRatingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateHotelRating")
    void updateRating(UpdateRatingDto updateRatingDto) throws AppBaseException;

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
     * @param ratingId identyfikator oceny hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("hideHotelRating")
    void changeVisibility(Long ratingId) throws AppBaseException;
}
