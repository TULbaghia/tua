package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący encji Hotel
 */
public class HotelException extends AppBaseException {
    private static final String DELETE_HOTEL_HAS_MANAGER = "exception.delete.hotel.has_manager";
    private static final String HOTEL_NOT_EXIST = "exception.hotel.not_exist";
    private static final String HOTEL_HAS_MANAGER = "exception.hotel.has_manager";
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

    /**
     * Wyjątek reprezentuje błąd podczas usuwania hotelu związany z przypisanym do niego managerem.
     *
     * @return wyjątek HotelException
     */
    public static HotelException deleteHasManager() {
        return new HotelException(DELETE_HOTEL_HAS_MANAGER);
    }

    /**
     * Wyjątek reprezentuje błąd podczas usuwania hotelu związany z nieistnieniem usuwanego hotelu.
     *
     * @return wyjątek HotelException
     */
    public static HotelException notExists() {
        return new HotelException(HOTEL_NOT_EXIST);
    }

    /**
     * Wyjątek reprezentuje błąd podczas przypisywania managera do hotelu związany z już przypisanym managerem.
     *
     * @return wyjątek HotelException
     */
    public static HotelException hasManager() {
        return new HotelException(HOTEL_HAS_MANAGER);
    }
}
