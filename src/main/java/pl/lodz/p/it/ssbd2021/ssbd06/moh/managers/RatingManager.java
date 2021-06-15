package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RatingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.HotelFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.RatingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private HotelFacade hotelFacade;

    @Inject
    private SecurityContext securityContext;

    /**
     * Zwraca ocenę hotelu
     *
     * @param id identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return obiekt oceny hotelu
     */
    @PermitAll
    public Rating get(Long id) throws AppBaseException {
        return Optional.ofNullable(ratingFacade.find(id)).orElseThrow(NotFoundException::ratingNotFound);
    }

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

        Long hotelId = ratedBooking.getBookingLineList().stream().findAny().get().getBox().getHotel().getId();
        BigDecimal hotelRating = calculateAverageRating(hotelId);
        Hotel hotel = hotelFacade.find(hotelId);
        hotel.setRating(hotelRating);
        hotelFacade.edit(hotel);

    }

    /**
     * Metoda odpowiedzialna za wyliczanie średniej wartości z ocen powiązanych z danym hotelem.
     *
     * @param hotelId id hotelu
     * @return średnia ocena hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"deleteHotelRating", "addHotelRating"})
    private BigDecimal calculateAverageRating(Long hotelId) throws AppBaseException {
        List<Rating> ratings = ratingFacade.getAllRatingsForHotelId(hotelId);
        if(ratings.isEmpty()) {
            return null;
        }
        return BigDecimal.valueOf(ratings.stream()
                .mapToDouble(Rating::getRate)
                .average()
                .getAsDouble()).setScale(1, RoundingMode.HALF_UP);
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
        Rating rating = get(ratingId);
        Account self = accountFacade.findByLogin(securityContext.getCallerPrincipal().getName());
        if (!rating.getCreatedBy().equals(self)) {
            throw RatingException.bookingNotOwned();
        }

        Optional<Hotel> optionalHotel = rating.getBooking().getBookingLineList()
                .stream()
                .limit(1)
                .map(BookingLine::getBox)
                .map(Box::getHotel)
                .findAny();

        ratingFacade.remove(rating);

        if (optionalHotel.isPresent()) {
            Hotel hotel = optionalHotel.get();
            hotel.setRating(calculateAverageRating(hotel.getId()));
            hotelFacade.edit(hotel);
        }
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId         dto z danymi hotelu
     * @param ratingVisibility poziom widoczności
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("hideHotelRating")
    public void changeVisibility(Long ratingId, RatingVisibility ratingVisibility) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
