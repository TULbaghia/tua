package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.RatingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager odpowiadający za zarządzanie ocenami hoteli.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RatingManager {

    @Inject
    private RatingFacade ratingFacade;

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @return lista ocen hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public List<Rating> getAll(Long hotelId) throws AppBaseException {
        List<Rating> allRatings = new ArrayList<>(ratingFacade.findAll());
        List<Rating> ratingsFromGivenHotel = new ArrayList<>();
        for (Rating rating : allRatings) {
            Box firstBox = rating.getBooking()
                    .getBookingLineList()
                    .stream()
                    .map(BookingLine::getBox)
                    .distinct()
                    .findFirst()
                    .orElseThrow(NotFoundException::boxNotFound);
            if (firstBox.getHotel().getId().equals(hotelId)) {
                ratingsFromGivenHotel.add(rating);
            }
        }
        return ratingsFromGivenHotel;
    }

    @PermitAll
    public Rating getRating(Long ratingId) throws AppBaseException {
        return ratingFacade.find(ratingId);
    }

    /**
     * Dodaje ocene
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotelRating")
    public void addRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateHotelRating")
    public void updateRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa ocenę hotelu
     *
     * @param ratingId identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteHotelRating")
    public void deleteRating(Long ratingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId         identyfikator oceny hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("hideHotelRating")
    public void changeVisibility(Long ratingId) throws AppBaseException {
        Rating rating = ratingFacade.find(ratingId);
        rating.setHidden(!rating.isHidden());
        ratingFacade.edit(rating);
    }
}
