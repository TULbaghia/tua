package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas podpisywania, weryfikacji podpisu lub wczytywania pliku konfiguracyjnego
 */
public class AppRuntimeException extends RuntimeException {

    private static final String SIGNER = "exception.runtime.signer";
    private static final String VERIFIER = "exception.runtime.verifier";
    private static final String CONFIG = "exception.runtime.config";
    private static final String JWT = "exception.runtime.jwt";

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas podpisywania obiektu lub inicjalizacji mechanizmu do podpisu
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static AppRuntimeException signerException(Throwable cause) {
        return new AppRuntimeException(SIGNER, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas weryfikacji podpisu lub inicjalizacji mechanizmu do weryfikowania
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static AppRuntimeException verifierException(Throwable cause) {
        return new AppRuntimeException(VERIFIER, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas wczytywania pliku konfiguracyjnego
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static AppRuntimeException configException(Throwable cause) {
        return new AppRuntimeException(CONFIG, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas wczytywania pliku konfiguracyjnego
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static AppRuntimeException configException() {
        return new AppRuntimeException(CONFIG);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas generowania, odnawiania lub weryfikacji tokenu JWT
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static AppRuntimeException jwtException(Throwable cause) {
        return new AppRuntimeException(JWT, cause);
    }
}
