package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.BookingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BoxFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager odpowiadający za zarządzanie rezerwacjami.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BookingManager {

    @Inject
    private BookingFacade bookingFacade;
    @Inject
    private BoxFacade boxFacade;
    @Inject
    private AccountFacade accountFacade;

    /**
     * Zwraca wskazaną rezerwację:
     * - Dla managera dozwolone rezerwacja w jego hotelu,
     * - Dla klienta rezerwacja złożona przez tego klienta.
     *
     * @param id identyfikator rezerwacji
     * @return rezerwacja
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    Booking get(Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę rezerwacji w zależności od roli:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    List<Booking> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę rezerwacji w zależności od roli z filtrowaniem:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    List<Booking> getAll(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Tworzy nową rezerwację
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("bookReservation")
    public void addBooking(NewBookingDto bookingDto, String username) throws AppBaseException {
        Account account = accountFacade.findByLogin(username);

        List<AnimalType> types = bookingDto.getBoxes().stream()
                .map(NewBookingDto.BookedBoxes::getType)
                .map(AnimalType::valueOf)
                .collect(Collectors.toList());
        List<Box> availableBoxes = boxFacade.getAvailableBoxesByTypesAndHotelId(bookingDto.getHotelId(), types);

        Booking booking = new Booking(bookingDto.getDateFrom(), bookingDto.getDateTo(), BigDecimal.valueOf(0), account, BookingStatus.PENDING);

        BigDecimal price = BigDecimal.ZERO;
        // todo should days between include the first day of booking ?
        long bookingDurationDays = Duration.between(booking.getDateFrom().toInstant(), booking.getDateTo().toInstant()).toDays();

        for (NewBookingDto.BookedBoxes typedBoxes : bookingDto.getBoxes()) {
            List<Box> boxes = availableBoxes.stream()
                    .filter(x -> x.getAnimalType().equals(AnimalType.valueOf(typedBoxes.getType())))
                    .limit(typedBoxes.getQuantity())
                    .collect(Collectors.toList());
            if (boxes.size() < typedBoxes.getQuantity()) {
                throw BookingException.notEnoughBoxesOfSpecifiedType();
            }
            for (Box box : boxes) {
                BookingLine bookingLine = new BookingLine(box.getPricePerDay(), booking, box);
                bookingLine.setCreatedBy(account);
                booking.getBookingLineList().add(bookingLine);
                price = price.add(box.getPricePerDay().multiply(BigDecimal.valueOf(bookingDurationDays)));
            }
        }
        booking.setPrice(price);
        booking.setCreatedBy(account);
        bookingFacade.create(booking);
    }

    /**
     * Anuluje rezerwację
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("cancelReservation")
    void cancelBooking(Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Kończy rezerwację
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("endReservation")
    void endBooking(Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmienia stan rezerwacji na IN_PROGRESS
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void startBooking(Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * MOH.18 Wyświetla listę aktywnych rezerwacji:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("getAllActiveReservations")
    List<Booking> showActiveBooking() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * MOH.19 Wyświetla listę archiwalnych rezerwacji:
     * - Dla managera archiwalne rezerwacje dotyczące jego hotelu,
     * - Dla klienta archiwalne rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("getAllArchiveReservations")
    List<Booking> showEndedBooking() throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
