package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.BookingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BoxFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.SecurityContext;
import java.util.Date;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;
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
    @Inject
    private SecurityContext securityContext;
    @Inject
    private EmailSender emailSender;
    @Inject
    private Config bookingConfig;

    /**
     * Zwraca wskazaną rezerwację:
     * - Dla managera dozwolone rezerwacja w jego hotelu,
     * - Dla klienta rezerwacja złożona przez tego klienta.
     *
     * @param id identyfikator rezerwacji
     * @return rezerwacja
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public Booking get(Long id) throws AppBaseException {
        return bookingFacade.find(id);
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
        List<Box> availableBoxes = boxFacade.getAvailableBoxesByTypesAndHotelId(bookingDto.getHotelId(), types, bookingDto.getDateFrom(), bookingDto.getDateTo());

        // not enough boxes to fulfill the booking or booking has been sent without any boxes
        if(availableBoxes.size() == 0){
            throw BookingException.notEnoughBoxesOfSpecifiedType();
        }

        Booking booking = new Booking(bookingDto.getDateFrom(), bookingDto.getDateTo(), BigDecimal.valueOf(0), account, BookingStatus.PENDING);

        BigDecimal price = BigDecimal.ZERO;
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
    public void cancelBooking(Long bookingId) throws AppBaseException {
        Booking booking = bookingFacade.find(bookingId);
        if (booking.getStatus().equals(BookingStatus.PENDING)) {
            if (securityContext.isCallerInRole("Client") && !isBetweenAllowedTimeLimit(booking.getCreationDate(), booking.getDateFrom())) {
                throw BookingException.timeForCancellationExceeded();
            }
            booking.setStatus(BookingStatus.CANCELLED);
            bookingFacade.edit(booking);
            emailSender.sendCancelReservationEmail(booking.getAccount(), booking.getId());
        } else if (booking.getStatus().equals(BookingStatus.IN_PROGRESS)) {
            throw BookingException.inProgressBookingCancellation();
        } else if (booking.getStatus().equals(BookingStatus.FINISHED)) {
            throw BookingException.finishedBookingCancellation();
        } else {
            throw BookingException.bookingAlreadyCancelled();
        }
    }

    /**
     * Sprawdza czy został zachowany dozwolony limit czas na anulowanie rezerwacji -
     * mniej niż 24h od złożenia rezerwacji i więcej niż 48h od rozpoczęcia rezerwacji
     *
     * @param bookingBeginDate data rozpoczęcia rezerwacji
     * @return czy został przekroczony dozwolony czas
     */
    private boolean isBetweenAllowedTimeLimit(Date bookingCreationDate, Date bookingBeginDate) {
        long timeFromCreationBooking = (new Date().getTime() - bookingCreationDate.getTime());
        long timeToBookingBegin = (bookingBeginDate.getTime() - new Date().getTime());

        return timeFromCreationBooking < bookingConfig.getDayInMillis() &&
                timeToBookingBegin > (bookingConfig.getDayInMillis() * 2);
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
    @RolesAllowed({"getAllActiveReservations", "Client"})
    public List<Booking> showActiveBooking() throws AppBaseException {
        String callerName = securityContext.getCallerPrincipal().getName();
        if (securityContext.isCallerInRole("Client")) {
            return bookingFacade.findAllActive().stream()
                    .filter(b -> b.getAccount().getLogin().equals(callerName))
                    .collect(Collectors.toList());
        } else {
            return bookingFacade.findAllActive().stream()
                    .filter(b -> b.getBookingLineList().stream().anyMatch(
                            bl -> bl.getBox().getHotel().getManagerDataList().stream().anyMatch(
                                    md -> md.getAccount().getLogin().equals(callerName))))
                    .collect(Collectors.toList());
        }
    }

    /**
     * MOH.19 Wyświetla listę archiwalnych rezerwacji:
     * - Dla managera archiwalne rezerwacje dotyczące jego hotelu,
     * - Dla klienta archiwalne rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"getAllArchiveReservations", "Client"})
    public List<Booking> showEndedBooking() throws AppBaseException {
        String callerName = securityContext.getCallerPrincipal().getName();
        if (securityContext.isCallerInRole("Client")) {
            return bookingFacade.findAllArchived().stream()
                    .filter(b -> b.getAccount().getLogin().equals(callerName))
                    .collect(Collectors.toList());
        } else {
            return bookingFacade.findAllArchived().stream()
                    .filter(b -> b.getBookingLineList().stream().anyMatch(
                            bl -> bl.getBox().getHotel().getManagerDataList().stream().anyMatch(
                                    md -> md.getAccount().getLogin().equals(callerName))))
                    .collect(Collectors.toList());
        }
    }
}
