package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class NotFoundException extends AppBaseException {

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
