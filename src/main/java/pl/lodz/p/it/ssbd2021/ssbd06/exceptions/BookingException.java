package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class BookingException extends AppBaseException{
    private static final String NOT_ENOUGH_BOXES = "exception.boxes.not_enough";

    protected BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BookingException(String message) {
        super(message);
    }

    public static BookingException notEnoughBoxesOfSpecifiedType() {
        return new BookingException(NOT_ENOUGH_BOXES);
    }
}
