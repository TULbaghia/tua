package pl.lodz.p.it.ssbd2021.ssbd06.security;

import org.apache.commons.codec.digest.DigestUtils;
import javax.security.enterprise.identitystore.PasswordHash;

public class ShaHash implements PasswordHash {
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
}
