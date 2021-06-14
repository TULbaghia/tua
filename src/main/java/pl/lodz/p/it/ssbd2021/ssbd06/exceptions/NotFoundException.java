package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się w sytuacji nieznalezienia encji
 */
public class NotFoundException extends AppBaseException {

    private static final String ACCOUNT_NOT_FOUND = "exception.not_found_exception.account_not_found";
    private static final String PENDING_CODE_NOT_FOUND = "exception.not_found_exception.pending_code_not_found";
    private static final String HOTEL_NOT_FOUND = "exception.not_found_exception.hotel_not_found";
    private static final String CITY_NOT_FOUND = "exception.not_found_exception.city_not_found";
    private static final String RATING_NOT_FOUND = "exception.not_found_exception.rating_not_found";

    private NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    private NotFoundException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji account.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException accountNotFound(Throwable cause) {
        return new NotFoundException(ACCOUNT_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji rating.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException ratingNotFound(Throwable cause) {
        return new NotFoundException(RATING_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji account.
     *
     * @return wyjątek NotFoundException
     */
    public static NotFoundException accountNotFound() {
        return new NotFoundException(ACCOUNT_NOT_FOUND);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji pending code.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException pendingCodeNotFound(Throwable cause) {
        return new NotFoundException(PENDING_CODE_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji Hotel.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException hotelNotFound(Throwable cause) {
        return new NotFoundException(HOTEL_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji City.
     *
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek NotFoundException
     */
    public static NotFoundException cityNotFound(Throwable cause) {
        return new NotFoundException(CITY_NOT_FOUND, cause);
    }

    /**
     * Tworzy wyjątek reprezentujący nieznalezienie encji City.
     *
     * @return wyjątek NotFoundException
     */
    public static NotFoundException cityNotFound() {
        return new NotFoundException(CITY_NOT_FOUND);
    }
}
