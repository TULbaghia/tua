package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class DatabaseQueryException extends AppBaseException {

    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseQueryException(String message) {
        super(message);
    }
}
