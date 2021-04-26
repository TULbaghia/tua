package pl.lodz.p.it.ssbd2021.ssbd06.security;

import pl.lodz.p.it.ssbd2021.ssbd06.utils.config.Config;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.text.ParseException;
import java.util.Date;

/**
 * Klasa służąca do generowania, odświeżania oraz weryfikacji JWT
 */
public class JWTGenerator {
    private static Config config;

    private static final String SECRET_KEY = config.getJWTSecretKey();
    private static final long JWT_EXPIRE_TIMEOUT = config.getJWTExpireTimeout();
    private static final String JWT_ISS = config.getJWTIss();

    public static String generateJWTString(CredentialValidationResult result) {
        try {
            final JWSSigner signer = new MACSigner(SECRET_KEY);

            final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(result.getCallerPrincipal().getName())
                    .claim("roles", String.join(",", result.getCallerGroups()))
                    .expirationTime(new Date(new Date().getTime() + JWT_EXPIRE_TIMEOUT))
                    .issuer(JWT_ISS)
                    .build();

            final SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
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
                    new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), newClaimsSet);
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
