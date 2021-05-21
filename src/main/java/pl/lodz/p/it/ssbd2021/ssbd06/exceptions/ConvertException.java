package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas konwertowania typów
 */
public class ConvertException extends AppBaseException {

    private static final String IP = "exception.convert.ip";

    private ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Wyjątek reprezentujący błąd występujący podczas konwertowania adresu IP
     *
     * @return wyjątek typu AppRuntimeException
     */
    public static ConvertException convertingIpException(Throwable cause) {
        return new ConvertException(IP, cause);
    }
}
