package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie rezerwacjami.
 */
@Path("/bookings")
public class BookingController extends AbstractController {
    /**
     * Zwraca wskazaną rezerwację:
     * - Dla managera dozwolone rezerwacja w jego hotelu,
     * - Dla klienta rezerwacja złożona przez tego klienta.
     *
     * @param id identyfikator rezerwacji
     * @throws AppBaseException gdy nie udało się pobrać rezerwacji
     * @return dto rezerwacji
     */
    @GET
    @Path("/{id}")
    public BookingDto get(@PathParam("id") Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę rezerwacji w zależności od roli:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException gdy nie udało się pobrać listy rezerwacji
     * @return lista dto rezerwacji
     */
    @GET
    public List<BookingDto> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę rezerwacji w zależności od roli z filtrowaniem:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem danych
     * @return lista dto rezerwacji
     */
    @GET
    @Path("/filter/{option}")
    public List<BookingDto> getAll(@PathParam("option") String option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Tworzy nową rezerwację
     *
     * @throws AppBaseException podczas problemu z tworzeniem rezerwacji
     */
    @POST
    @RolesAllowed("bookReservation")
    public void addBooking(NewBookingDto bookingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Anuluje rezerwację
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas problemu z anulowaniem rezerwacji
     */
    @PATCH
    @RolesAllowed("cancelReservation")
    @Path("/cancel/{id}")
    public void cancelBooking(@PathParam("id") Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Kończy rezerwację
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas problemu z zakończeniem rezerwacji
     */
    @PATCH
    @RolesAllowed("endReservation")
    @Path("/end/{id}")
    public void endBooking(@PathParam("id") Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmienia stan rezerwacji na IN_PROGRESS
     *
     * @param bookingId identyfikator rezerwacji
     * @throws AppBaseException podczas błędu związanego rozpoczęciem rezerwacji
     */
    @PATCH
    @Path("/start/{id}")
    public void startBooking(@PathParam("id") Long bookingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyświetla listę aktywnych rezerwacji:
     * - Dla managera rezerwacje dotyczące jego hotelu,
     * - Dla klienta rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem aktywnych rezerwacji
     * @return lista dto aktywnych rezerwacji
     */
    @GET
    @RolesAllowed("getAllActiveReservations")
    @Path("/active")
    public List<BookingDto> showActiveBooking() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyświetla listę archiwalnych rezerwacji:
     * - Dla managera archiwalne rezerwacje dotyczące jego hotelu,
     * - Dla klienta archiwalne rezerwacje dotyczące tego klienta.
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem zakończonych rezerwacji
     * @return lista dto zakończonych rezerwacji
     */
    @GET
    @RolesAllowed("getAllArchiveReservations")
    @Path("/ended")
    public List<BookingDto> showEndedBooking() throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
