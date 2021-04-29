package pl.lodz.p.it.ssbd2021.ssbd06.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Klasa umożliwiająca haszowanie algorytmem sha256
 */
public class PasswordHasher {

    /**
     * Przekształca hasło do postaci niejawnej
     * @param password hasło
     * @return hasło w postaci niejawnej
     */
    public static String sha256Hash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
