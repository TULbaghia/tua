package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BookingEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie rezerwacjami.
 */
@Path("/bookings")
public class BookingController extends AbstractController {

    @Inject
    private BookingEndpointLocal bookingEndpoint;

    @Inject
    private MessageSigner messageSigner;

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
    public Response get(@PathParam("id") Long id) throws AppBaseException {
        BookingDto bookingDto = repeat(() -> bookingEndpoint.get(id), bookingEndpoint);
        return Response.ok()
                .entity(bookingDto)
                .header("ETag", messageSigner.sign(bookingDto))
                .build();
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
    @EtagValidatorFilterBinding
    @Path("/cancel/{id}")
    public void cancelBooking(@PathParam("id") Long bookingId) throws AppBaseException {
        repeat(() -> bookingEndpoint.cancelBooking(bookingId), bookingEndpoint);
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
        return repeat(() -> bookingEndpoint.showActiveBooking(), bookingEndpoint);
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
        return repeat(() -> bookingEndpoint.showEndedBooking(), bookingEndpoint);
    }
}
