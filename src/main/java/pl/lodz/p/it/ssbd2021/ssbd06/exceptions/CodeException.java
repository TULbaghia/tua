package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd dotyczący kodów aktywacyjnych
 */
public class CodeException extends AppBaseException {
    private static final String CODE_EXPIRED = "exception.code.code_expired";
    private static final String CODE_INVALID = "exception.code.code_invalid";
    private static final String CODE_USED = "exception.code.code_used";

    private CodeException(String message, Throwable cause) {
        super(message, cause);
    }

    private CodeException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący nieaktualność kodu.
     * @return wyjątek CodeException
     */
    public static CodeException codeExpired() {
        return new CodeException(CODE_EXPIRED);
    }

    /**
     * Tworzy wyjątek reprezentujący nieprawidłowość przekazanego kodu.
     * @return wyjątek CodeException
     */
    public static CodeException codeInvalid() {
        return new CodeException(CODE_INVALID);
    }

    /**
     * Tworzy wyjątek reprezentujący użyty kod.
     * @return wyjątek CodeException
     */
    public static CodeException codeUsed() {
        return new CodeException(CODE_USED);
    }
}
