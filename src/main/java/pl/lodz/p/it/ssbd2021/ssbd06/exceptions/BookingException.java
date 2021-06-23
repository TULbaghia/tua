package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;


/**
 * Reprezentuje błąd dotyczący encji Booking
 */
public class BookingException extends AppBaseException {
    private static final String NOT_ENOUGH_BOXES = "exception.boxes.not_enough";
    private static final String BOXES_NOT_AVAILABLE = "exception.boxes.not_available";
    private static final String CANCELLED_BOOKING = "exception.booking.cancelled_booking";
    private static final String FINISHED_BOOKING_CANCEL = "exception.booking.finished_booking";
    private static final String IN_PROGRESS_BOOKING_CANCEL = "exception.booking.in_progress_booking";
    private static final String TIME_EXCEEDED = "exception.booking.time_exceeded";
    private static final String ACCESS_DENIED = "exception.booking.access_denied";
    private static final String NOT_STARTED_BOOKING = "exception.booking.not_started_booking";
    private static final String INVALID_DATE_RANGE = "exception.booking.invalid_date_range";
    private static final String TOO_EARLY_TO_START = "exception.booking.too_early";
    private static final String DATE_FROM_PAST = "exception.booking.date_from_past";

    protected BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BookingException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek występujący podczas składania rezerwacji gdy brakuje klatek aby dokończyć proces rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException notEnoughBoxesOfSpecifiedType() {
        return new BookingException(NOT_ENOUGH_BOXES);
    }

    /**
     * Tworzy wyjątek występujący podczas składania rezerwacji na klatki które nie są dostępne
     *
     * @return wyjątek BookingException.
     */
    public static BookingException boxesNotAvailable() {
        return new BookingException(BOXES_NOT_AVAILABLE);
    }

    /**
     * Tworzy wyjątek występujący podczas podania dat nie tworzących poprawnego zakresu
     *
     * @return wyjątek BookingException.
     */
    public static BookingException invalidDateRange() {
        return new BookingException(INVALID_DATE_RANGE);
    }

    /**
     * Tworzy wyjątek występujący podczas podania daty z przeszłości lub daty której doba hotelowa już się zaczęła
     *
     * @return wyjątek BookingException.
     */
    public static BookingException dateFromPast() {
        return new BookingException(DATE_FROM_PAST);
    }

    /**
     * Tworzy wyjątek występujący podczas anulowania anulowanej rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException bookingAlreadyCancelled() {
        return new BookingException(CANCELLED_BOOKING);
    }

    /**
     * Tworzy wyjątek występujący podczas anulowania zakończonej rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException finishedBookingCancellation() {
        return new BookingException(FINISHED_BOOKING_CANCEL);
    }

    /**
     * Tworzy wyjątek występujący podczas anulowania trwającej rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException inProgressBookingCancellation() {
        return new BookingException(IN_PROGRESS_BOOKING_CANCEL);
    }

    /**
     * Tworzy wyjątek występujący podczas próby anulowania rezerwacji, która rozpoczyna się za mniej niż 10 dni.
     *
     * @return wyjątek BookingException.
     */
    public static BookingException timeForCancellationExceeded() {
        return new BookingException(TIME_EXCEEDED);
    }

    /**
     * Tworzy wyjątek występujący podczas próby zmiany statusu rezerwacji na IN_PROGRESS, jeśli data początku
     * rezerwacji jest większa od aktualnej
     *
     * @return wyjątek BookingException.
     */
    public static BookingException toEarlyToStart() {
        return new BookingException(TOO_EARLY_TO_START);
    }

    /**
     * Tworzy wyjątek występujący podczas próby anulowania rezerwacji klienta przez innego klienta.
     *
     * @return wyjątek BookingException.
     */
    public static BookingException accessDenied() {
        return new BookingException(ACCESS_DENIED);
    }

    /**
     * Tworzy wyjątek występujący podczas próby zakończenia zakończonej już rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException bookingAlreadyFinished() {
        return new BookingException(FINISHED_BOOKING_CANCEL);
    }

    /**
     * Tworzy wyjątek występujący podczas próby zakończenia nierozpoczętej rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException bookingNotStartedYet() {
        return new BookingException(NOT_STARTED_BOOKING);
    }

    /**
     * Tworzy wyjątek występujący podczas próby zakończenia anulowanej rezerwacji
     *
     * @return wyjątek BookingException.
     */
    public static BookingException bookingCancelledBeforeStart() {
        return new BookingException(CANCELLED_BOOKING);
    }
}
