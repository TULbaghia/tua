package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.security.enterprise.identitystore.PasswordHash;

//TODO: Rozwiązać podczas merge z soterią
public class ShaHash implements PasswordHash {
    @Override
    public String generate(char[] password) {
        return "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd";
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        return false;
    }
}
