package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący encji Box
 */
public class BoxException extends AppBaseException{

    private static final String ACCESS_DENIED = "exception.box.access_denied";

    protected BoxException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BoxException(String message) {
        super(message);
    }

    /**
     * Błąd zwiażany z brakień uprawnień do modyfikacji encji
     *
     * @return wyjątek BoxException
     */
    public static BoxException accessDenied() {
        return new BoxException(ACCESS_DENIED);
    }
}
