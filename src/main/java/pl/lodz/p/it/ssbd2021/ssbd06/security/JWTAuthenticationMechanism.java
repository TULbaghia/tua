package pl.lodz.p.it.ssbd2021.ssbd06.security;

import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * Klasa służąca weryfikacji żądań
 */
@RequestScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Inject
    JWTGenerator jwtGenerator;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext) {

        String authHeader = httpServletRequest.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return httpMessageContext.doNothing();
        }

        String tokenToValidate = authHeader.substring(BEARER.length());
        if (jwtGenerator.validateJWT(tokenToValidate)) {
            try {
                SignedJWT jwtToken = SignedJWT.parse(tokenToValidate);
                String login = jwtToken.getJWTClaimsSet().getSubject();
                String roles = jwtToken.getJWTClaimsSet().getStringClaim("roles");
                Date expirationTime = (Date) (jwtToken.getJWTClaimsSet().getClaim("exp"));
                String iss = jwtToken.getJWTClaimsSet().getIssuer();

                if (new Date().after(expirationTime)) {
                    return httpMessageContext.responseUnauthorized();
                }
                return httpMessageContext
                        .notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(roles.split(","))));
            } catch (ParseException e) {
                return httpMessageContext.responseUnauthorized();
            }
        }
        return httpMessageContext.responseUnauthorized();
    }
}
