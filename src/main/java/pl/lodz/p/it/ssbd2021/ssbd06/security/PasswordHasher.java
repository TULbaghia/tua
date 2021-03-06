package pl.lodz.p.it.ssbd2021.ssbd06.security;

import org.apache.commons.codec.digest.DigestUtils;

import javax.security.enterprise.identitystore.PasswordHash;

/**
 * Klasa umożliwiająca haszowanie algorytmem sha256
 */
public class PasswordHasher implements PasswordHash {

    @Override
    public String generate(char[] password) {
        return DigestUtils.sha256Hex(new String(password));
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        String toVerify = DigestUtils.sha256Hex(new String(password));
        if(hashedPassword.equals(toVerify)) {
            return true;
        }
        else return false;
    }

    /**
     * Przekształca hasło do postaci niejawnej
     * @param password hasło
     * @return hasło w postaci niejawnej
     */
    public static String generate(String password) {
        return DigestUtils.sha256Hex(password);
    }

    /**
     * Sprawdza zgodność hasła w postaci jawnej z hasłem w postaci niejawnej.
     * @param password hasło w postaci jawnej
     * @param hashedPassword hasło w postaci niejawnej
     * @return wynik operacji jako prawda/fałsz
     */
    public static boolean check(String password, String hashedPassword) {
        String toVerify = generate(password);
        if(hashedPassword.equals(toVerify)) {
            return true;
        }
        else return false;
    }
}
