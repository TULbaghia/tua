package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje wyjątek dotyczący encji Box
 */
public class BoxException extends AppBaseException{

    private static final String ACCESS_DENIED = "exception.box.access_denied";
    private static final String BOX_IN_USED = "exception.box.box_in_used";
    private static final String BOX_IS_PENDING = "exception.box.box_is_pending";

    protected BoxException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BoxException(String message) {
        super(message);
    }

    /**
     * Wyjątek związany z brakień uprawnień do modyfikacji encji
     *
     * @return wyjątek BoxException
     */
    public static BoxException accessDenied() {
        return new BoxException(ACCESS_DENIED);
    }

    /**
     * Wyjątek związany z uczestniczeniem boxa w trwającej rezerwacji
     *
     * @return wyjątek BoxException
     */
    public static BoxException boxIsUsed() {
        return new BoxException(BOX_IN_USED);
    }

    /**
     * Wyjątek związany z faktem rezerwacji boxa
     *
     * @return wyjątek BoxException
     */
    public static BoxException boxIsPending() {
        return new BoxException(BOX_IS_PENDING);
    }

}
