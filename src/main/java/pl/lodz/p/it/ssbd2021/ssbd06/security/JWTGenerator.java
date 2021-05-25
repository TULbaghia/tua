package pl.lodz.p.it.ssbd2021.ssbd06.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppRuntimeException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;

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
    private Config jwtConfig;

    private static String SECRET_KEY;
    private static long JWT_EXPIRE_TIMEOUT;
    private static String JWT_ISS;

    @PostConstruct
    private void init() {
        SECRET_KEY = jwtConfig.getJwtSecretKey();
        JWT_EXPIRE_TIMEOUT = jwtConfig.getJwtExpireTimeout();
        JWT_ISS = jwtConfig.getJwtIss();
    }

    /**
     * Generuje token JWT w formacie String
     * @param result
     * @return wygenerowany token
     */
    public String generateJWTString(CredentialValidationResult result) {
        try {
            final JWSSigner signer = new MACSigner(SECRET_KEY);
            final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(result.getCallerPrincipal().getName())
                    .claim("roles", String.join(",", result.getCallerGroups()))
                    .expirationTime(new Date(new Date().getTime() + JWT_EXPIRE_TIMEOUT))
                    .issuer(JWT_ISS)
                    .build();

            final SignedJWT signedJWT =
                    new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
                            claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    /**
     * Odświeża podany token JWT
     * @param tokenToUpdate token przeznaczony do odświeżenia
     * @return nowy token JWT
     */
    public String updateJWTString(String tokenToUpdate) {
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
                    new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
                            newClaimsSet);
            newSignedJWT.sign(signer);
            return newSignedJWT.serialize();
        } catch (ParseException | JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    /**
     * Przeprowadza proces walidacji tokenu JWT
     * @param tokenToValidate token przeznaczony do walidacji
     * @return wartość prawda/fałsz reprezentująca poprawność tokenu JWT
     */
    public boolean validateJWT(String tokenToValidate) {
        try {
            JWSObject jwsObject = JWSObject.parse(tokenToValidate);
            JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY);
            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }

    public String getCallerGroups(String token) {
        try {
            SignedJWT jwtToken = SignedJWT.parse(token);
            return jwtToken.getJWTClaimsSet().getStringClaim("roles");
        } catch (ParseException e) {
            throw AppRuntimeException.jwtException(e);
        }
    }
}
