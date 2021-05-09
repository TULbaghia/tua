package pl.lodz.p.it.ssbd2021.ssbd06.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.ETagConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;

@ApplicationScoped
public class MessageVerifier {

    @Inject
    private ETagConfig config;

    public boolean validateEntitySignature(String eTag) {
        try {
            final JWSObject jwsObject = JWSObject.parse(eTag);
            final JWSVerifier verifier = new MACVerifier(config.getEtagSecretKey());
            return jwsObject.verify(verifier);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }
}
