package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.DetailBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie rezerwacjami.
 */
@Local
public interface BookingEndpointLocal extends CallingClass {
    /**
     * Zwraca wskazaną rezerwację:
     * - Dla managera dozwolone rezerwacja w jego hotelu,
     * - Dla klienta rezerwacja złożona przez tego klienta.
     *
     * @param id identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return rezerwacja
     */
    DetailBookingDto get(Long id) throws AppBaseException;

    /**
     * Zwraca listę rezerwacji w zależności od roli:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista rezerwacji
     */
    List<BookingDto> getAll() throws AppBaseException;

    /**
     * Zwraca listę rezerwacji w zależności od roli z filtrowaniem:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista rezerwacji
     */
    List<BookingDto> getAll(String ...option) throws AppBaseException;

    /**
     * Tworzy nową rezerwację (aktor klient)
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("bookReservation")
    void addBooking(NewBookingDto bookingDto) throws AppBaseException;

    /**
     * Anuluje rezerwację (aktor klient lub manager)
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("cancelReservation")
    void cancelBooking(Long bookingId) throws AppBaseException;

    /**
     * Kończy rezerwację (aktor manager)
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("endReservation")
    void endBooking(Long bookingId) throws AppBaseException;

    /**
     * Zmienia stan rezerwacji na IN_PROGRESS (aktor manager)
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("startReservation")
    void startBooking(Long bookingId) throws AppBaseException;

    /**
     * MOH.18 Wyświetla listę aktywnych rezerwacji:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista rezerwacji
     */
    @RolesAllowed("getAllActiveReservations")
    List<BookingDto> showActiveBooking() throws AppBaseException;

    /**
     * MOH.19 Wyświetla listę archiwalnych rezerwacji:
     * - Dla managera archiwalne rezerwacje dotyczące jego hotelu,
     * - Dla klienta archiwalne rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista rezerwacji
     */
    @RolesAllowed("getAllArchiveReservations")
    List<BookingDto> showEndedBooking() throws AppBaseException;

    /**
     * Wyświetla listę archiwalnych rezerwacji bez oceny dotyczących klienta i związanych z danym hotelem.
     * @param hotelId id hotelu
     * @return lista zakończonych rezerwacji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("getEndedBookingsForHotel")
    List<BookingDto> showUnratedEndedBookingsForHotel(Long hotelId) throws AppBaseException;
}
