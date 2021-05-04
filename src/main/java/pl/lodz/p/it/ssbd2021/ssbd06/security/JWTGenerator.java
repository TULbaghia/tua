package pl.lodz.p.it.ssbd2021.ssbd06.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.JWTConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.text.ParseException;
import java.util.Date;

/**
 * Klasa służąca do generowania, odświeżania oraz weryfikacji JWT
 */
@RequestScoped
public class JWTGenerator {
    @Inject
    private JWTConfig config;

    private static String SECRET_KEY;
    private static long JWT_EXPIRE_TIMEOUT;
    private static String JWT_ISS;

    @PostConstruct
    private void init() {
        this.SECRET_KEY = config.getJWTSecretKey();
        this.JWT_EXPIRE_TIMEOUT = config.getJWTExpireTimeout();
        this.JWT_ISS = config.getJWTIss();
    }

    public static String generateJWTString(CredentialValidationResult result) {
        try {
            final JWSSigner signer = new MACSigner(SECRET_KEY);

            final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(result.getCallerPrincipal().getName())
                    .claim("roles", String.join(",", result.getCallerGroups()))
                    .expirationTime(new Date(new Date().getTime() + JWT_EXPIRE_TIMEOUT))
                    .issuer(JWT_ISS)
                    .build();

            final SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return "JWT error";
        }
    }

    public static String updateJWTString(String tokenToUpdate) {
        try {
            final JWSSigner signer = new MACSigner(SECRET_KEY);
            SignedJWT oldToken = SignedJWT.parse(tokenToUpdate);
            JWTClaimsSet oldClaimSet = oldToken.getJWTClaimsSet();

            final JWTClaimsSet newClaimsSet = new JWTClaimsSet.Builder()
                    .subject(oldClaimSet.getSubject())
                    .claim("roles", oldClaimSet.getClaim("roles"))
                    .expirationTime(new Date(new Date().getTime() + JWT_EXPIRE_TIMEOUT))
                    .issuer(JWT_ISS)
                    .build();

            final SignedJWT newSignedJWT =
                    new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), newClaimsSet);
            newSignedJWT.sign(signer);
            return newSignedJWT.serialize();
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return "JWT error";
        }
    }

    public static boolean validateJWT(String tokenToValidate) {
        try {
            JWSObject jwsObject = JWSObject.parse(tokenToValidate);
            JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY);
            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }
}
