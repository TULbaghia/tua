package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.BookingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacadeMoh;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BoxFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manager odpowiadający za zarządzanie rezerwacjami.
 */
@DeclareRoles("Client")
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class BookingManager {

    @Inject
    private BookingFacade bookingFacade;
    @Inject
    private BoxFacade boxFacade;
    @Inject
    private AccountFacadeMoh accountFacadeMoh;
    @Resource(name = "sessionContext")
    SessionContext sessionContext;
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
    @RolesAllowed({"startReservation", "cancelReservation", "endReservation", "getReservation"})
    public Booking get(Long id) throws AppBaseException {
        return bookingFacade.find(id);
    }

    /**
     * Tworzy nową rezerwację
     *
     * @param bookingDto dane nowej rezerwacji
     * @param username nazwa użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("bookReservation")
    public void addBooking(NewBookingDto bookingDto, String username) throws AppBaseException {
        Date dateFrom = new Date(bookingDto
        .getDateFrom()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant()
                .plus(14, ChronoUnit.HOURS)
                .toEpochMilli());
        Date dateTo = new Date(bookingDto
        .getDateTo()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant()
                .plus(12, ChronoUnit.HOURS)
                .toEpochMilli());


        Date now = new Date(new Date().toInstant().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).toInstant().plus(1, ChronoUnit.DAYS).plus(14, ChronoUnit.HOURS).toEpochMilli());


        if(!(now.equals(dateFrom) || now.before(dateFrom))){
            throw BookingException.dateFromPast();
        }

        if(dateFrom.after(dateTo)){
            throw BookingException.invalidDateRange();
        }

        Account account = accountFacadeMoh.findByLogin(username);

        List<Box> availableBoxes = boxFacade
                .getAvailableBoxesByIdListAndHotelIdWithLock(bookingDto.getHotelId(), bookingDto.getBoxes(), dateFrom, dateTo);
        // not enough boxes to fulfill the booking or booking has been sent without any boxes
        if (availableBoxes.size() != bookingDto.getBoxes().size()) {
            throw BookingException.notEnoughBoxesOfSpecifiedType();
        }

        Booking booking = new Booking(dateFrom, dateTo, BigDecimal.valueOf(0), account, BookingStatus.PENDING);

        BigDecimal price = BigDecimal.ZERO;
        long bookingDurationDays = Duration.between(dateFrom.toInstant(), dateTo.toInstant()).toDays() + 1;

        for (Box box : availableBoxes) {
            if (bookingDto.getBoxes().stream().noneMatch(x -> Objects.equals(x, box.getId()))) {
                throw BookingException.boxesNotAvailable();
            }
            BookingLine bookingLine = new BookingLine(box.getPricePerDay(), booking, box);
            bookingLine.setCreatedBy(account);
            booking.getBookingLineList().add(bookingLine);
            price = price.add(box.getPricePerDay().multiply(BigDecimal.valueOf(bookingDurationDays)));
        }

        booking.setPrice(price);
        booking.setCreatedBy(account);
        bookingFacade.create(booking);
        emailSender.sendCreateReservationEmail(booking.getAccount(), booking.getId());
    }

    /**
     * Anuluje rezerwację
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"cancelReservation"})
    public void cancelBooking(Long bookingId) throws AppBaseException {
        Booking booking = bookingFacade.find(bookingId);
        if (booking.getStatus().equals(BookingStatus.PENDING)) {
            if (sessionContext.isCallerInRole("Client") &&
                    !isBetweenAllowedTimeLimit(booking.getCreationDate(), booking.getDateFrom())) {
                throw BookingException.timeForCancellationExceeded();
            }
            Account modifier = booking.getAccount();
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setModifiedBy(modifier);
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
     * @param bookingCreationDate data utworzenia rezerwacji
     * @param bookingBeginDate    data rozpoczęcia rezerwacji
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
    public void endBooking(Long bookingId) throws AppBaseException {
        Booking booking = bookingFacade.find(bookingId);
        if (booking.getStatus().equals(BookingStatus.IN_PROGRESS)) {
            booking.setStatus(BookingStatus.FINISHED);
            booking.setModifiedBy(accountFacadeMoh.findByLogin(sessionContext.getCallerPrincipal().getName()));
            bookingFacade.edit(booking);
        } else if (booking.getStatus().equals(BookingStatus.FINISHED)) {
            throw BookingException.bookingAlreadyFinished();
        } else if (booking.getStatus().equals(BookingStatus.PENDING)) {
            throw BookingException.bookingNotStartedYet();
        } else if (booking.getStatus().equals(BookingStatus.CANCELLED)) {
            throw BookingException.bookingCancelledBeforeStart();
        }
    }

    /**
     * Zmienia stan rezerwacji na IN_PROGRESS
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("startReservation")
    public void startBooking(Long bookingId) throws AppBaseException {
        Booking booking = bookingFacade.find(bookingId);
        if (booking.getStatus().equals(BookingStatus.PENDING) && ifReservationCanBeStarted(booking.getDateFrom())) {
            Account modifier = accountFacadeMoh.findByLogin(sessionContext.getCallerPrincipal().getName());
            booking.setStatus(BookingStatus.IN_PROGRESS);
            booking.setModifiedBy(modifier);
            bookingFacade.edit(booking);
        } else if (booking.getStatus().equals(BookingStatus.IN_PROGRESS)) {
            throw BookingException.inProgressBookingCancellation();
        } else if (booking.getStatus().equals(BookingStatus.FINISHED)) {
            throw BookingException.finishedBookingCancellation();
        } else if (booking.getStatus().equals(BookingStatus.CANCELLED)){
            throw BookingException.bookingAlreadyCancelled();
        } else {
            throw BookingException.toEarlyToStart();
        }
    }

    /**
     * Sprawdza czy aktualna data jest równa lub mniejsza od daty rozpoczęcia rezerwacji
     *
     * @param bookingBeginDate data rozpoczęcia rezerwacji
     * @return czy rezerwacja może zmienić status na w trakcie
     */
    private boolean ifReservationCanBeStarted(Date bookingBeginDate) {
        long actualDateInMillis = new Date().getTime();
        return bookingBeginDate.getTime() <= actualDateInMillis;
    }

    /**
     * MOH.18 Wyświetla listę aktywnych rezerwacji:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"getAllActiveReservations"})
    public List<Booking> showActiveBooking() throws AppBaseException {
        String callerName = sessionContext.getCallerPrincipal().getName();
        if (sessionContext.isCallerInRole("Client")) {
            return bookingFacade.findAllActive().stream()
                    .filter(b -> b.getAccount().getLogin().equals(callerName))
                    .collect(Collectors.toList());
        } else {
            return bookingFacade.findAllActive().stream()
                    .filter(b -> b.getBookingLineList().stream().anyMatch(
                            bl -> bl.getBox().getHotel() != null && bl.getBox().getHotel().getManagerDataList().stream().anyMatch(
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
    @RolesAllowed({"getAllArchiveReservations"})
    public List<Booking> showEndedBooking() throws AppBaseException {
        String callerName = sessionContext.getCallerPrincipal().getName();
        if (sessionContext.isCallerInRole("Client")) {
            return bookingFacade.findAllArchived().stream()
                    .filter(b -> b.getAccount().getLogin().equals(callerName))
                    .collect(Collectors.toList());
        } else {
            return bookingFacade.findAllArchived().stream()
                    .filter(b -> b.getBookingLineList().stream().anyMatch(
                            bl -> bl.getBox().getHotel() != null && bl.getBox().getHotel().getManagerDataList().stream().anyMatch(
                                    md -> md.getAccount().getLogin().equals(callerName))))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyświetla listę archiwalnych rezerwacji bez oceny dotyczących klienta i związanych z danym hotelem.
     *
     * @param hotelId id hotelu
     * @return lista rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("getEndedBookingsForHotel")
    public List<Booking> showUnratedEndedBookingsForHotel(Long hotelId) throws AppBaseException {
        String callerName = sessionContext.getCallerPrincipal().getName();
        return bookingFacade.findAllArchived().stream()
                .filter(b -> b.getAccount().getLogin().equals(callerName)
                        &&
                        b.getBookingLineList().stream().anyMatch(bl -> bl.getBox().getHotel() != null && bl.getBox().getHotel().getId().equals(hotelId))
                        && b.getRating() == null)
                .collect(Collectors.toList());
    }
}
