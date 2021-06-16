package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący encji Rating
 */
public class RatingException extends AppBaseException {
    private static final String RATING_ALREADY_EXISTS = "exception.rating_exception.rating_already_exists";
    private static final String BOOKING_NOT_OWNED = "exception.rating_exception.booking_not_owned";
    private static final String BOOKING_NOT_EXISTS = "exception.rating_exception.booking_not_exists";
    private static final String BOOKING_NOT_FINISHED = "exception.rating_exception.booking_not_finished";
    private static final String ACCESS_DENIED = "exception.rating.access_denied";

    private RatingException(String message) {
        super(message);
    }

    /**
     * Wyjątek reprezentuje błąd związany z istnieniem już oceny dla danej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException ratingAlreadyExists() {
        return new RatingException(RATING_ALREADY_EXISTS);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny nieposiadanej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException bookingNotOwned() {
        return new RatingException(BOOKING_NOT_OWNED);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny nieistniejącej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException bookingNotExists() {
        return new RatingException(BOOKING_NOT_EXISTS);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą oceny niezakończonej rezerwacji.
     *
     * @return wyjątek RatingException
     */
    public static RatingException bookingNotFinished() {
        return new RatingException(BOOKING_NOT_FINISHED);
    }

    /**
     * Wyjątek reprezentuje błąd związany z próbą dostępu do zabronionego zasobu.
     *
     * @return wyjątek RatingException
     */
    public static RatingException accessDenied(){
        return new RatingException(ACCESS_DENIED);
    }

}
