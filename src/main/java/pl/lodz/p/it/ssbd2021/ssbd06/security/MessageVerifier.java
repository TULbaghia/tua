package pl.lodz.p.it.ssbd2021.ssbd06.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.SignatureException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;

/**
 * Klasa służąca do weryfikacji podpisu
 */
@ApplicationScoped
public class MessageVerifier {

    @Inject
    private Config eTagConfig;

    private JWSVerifier verifier;

    /**
     * Metoda inicjalizująca mechanizm weryfikacji
     */
    @PostConstruct
    public void init() {
        try {
            verifier = new MACVerifier(eTagConfig.getEtagSecretKey());
        } catch (JOSEException e) {
            throw SignatureException.verifierException(e);
        }
    }

    /**
     * Metoda weryfikująca podpis wiadomości
     *
     * @param message wiadomość do weryfikacji
     * @return rezultat weryfikacji
     */
    public boolean validateSignature(String message) {
        try {
            final JWSObject jwsObject = JWSObject.parse(message);
            return jwsObject.verify(verifier);
        } catch (ParseException | JOSEException e) {
            throw SignatureException.verifierException(e);
        }
    }
}
