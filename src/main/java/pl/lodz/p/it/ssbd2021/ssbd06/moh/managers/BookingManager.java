package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.BookingException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.BookingFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import java.util.ArrayList;
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
    private AccountFacade accountFacade;

    @Inject
    private SecurityContext securityContext;

    /**
     * Zwraca wskazaną rezerwację:
     * - Dla managera dozwolone rezerwacja w jego hotelu,
     * - Dla klienta rezerwacja złożona przez tego klienta.
     *
     * @param id identyfikator rezerwacji
     * @return rezerwacja
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    public Booking get(Long id) throws AppBaseException {
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
    void addBooking(NewBookingDto bookingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
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
    public void endBooking(Long bookingId) throws AppBaseException {
        Booking booking = bookingFacade.find(bookingId);
        if (booking.getStatus().equals(BookingStatus.IN_PROGRESS)) {
            booking.setStatus(BookingStatus.FINISHED);
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
