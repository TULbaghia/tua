package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas podpisywania lub weryfikacji podpisu
 */
public class SignatureException extends RuntimeException {

    private static final String SIGNER = "exception.signature.signer";
    private static final String VERIFIER = "exception.signature.verifier";

    private SignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas podpisywania obiektu lub inicjalizacji mechanizmu do podpisu
     *
     * @return wyjątek typu SignatureException
     */
    public static SignatureException signerException(Throwable cause) {
        return new SignatureException(SIGNER, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas weryfikacji podpisu lub inicjalizacji mechanizmu do weryfikowania
     *
     * @return wyjątek typu SignatureException
     */
    public static SignatureException verifierException(Throwable cause) {
        return new SignatureException(VERIFIER, cause);
    }
}
