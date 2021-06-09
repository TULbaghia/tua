package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd podczas interakcji z obiektami typu Hotel.
 */
public class HotelException extends AppBaseException {

    private static final String HOTEL_NAME_INVALID = "exception.hotel.name_invalid";

    private HotelException(String message, Throwable cause) {
        super(message, cause);
    }

    private HotelException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek związany z duplikacją nazwy obiektu typu Hotel.
     *
     * @param cause wyjątek, który zostanie opakowany.
     * @return wyjątek NotFoundException.
     */
    public static HotelException hotelNameExists(Throwable cause) {
        return new HotelException(HOTEL_NAME_INVALID, cause);
    }
}
