package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RatingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.RatingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
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

    @Inject
    private BookingFacade bookingFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private SecurityContext securityContext;

    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista ocen hotelu
     */
    List<Rating> getAll(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje ocene
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotelRating")
    public void addRating(NewRatingDto ratingDto) throws AppBaseException {
        Booking ratedBooking = bookingFacade.find(ratingDto.getBookingId());
        if(ratedBooking == null) {
            throw RatingException.bookingNotExists();
        }
        if(ratedBooking.getStatus() != BookingStatus.FINISHED) {
            throw RatingException.bookingNotFinished();
        }
        if (ratedBooking.getRating() != null) {
            throw RatingException.ratingAlreadyExists();
        }

        Account clientAccount = accountFacade.findByLogin(securityContext.getCallerPrincipal().getName());
        if (!ratedBooking.getAccount().getId().equals(clientAccount.getId())) {
            throw RatingException.bookingNotOwned();
        }

        Rating rating = new Rating(ratingDto.getRate(), false);
        if(ratingDto.getComment() != null) {
            rating.setComment(ratingDto.getComment());
        }

        rating.setBooking(ratedBooking);
        rating.setCreatedBy(clientAccount);
        ratingFacade.create(rating);
    }

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateHotelRating")
    void updateRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa ocenę hotelu
     *
     * @param ratingId identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteHotelRating")
    void deleteRating(Long ratingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId dto z danymi hotelu
     * @param ratingVisibility poziom widoczności
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("hideHotelRating")
    void changeVisibility(Long ratingId, RatingVisibility ratingVisibility) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
