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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Klasa służąca weryfikacji żądań
 */
@RequestScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static Map<String, Set<String>> mapRoles = Map.of(
            "CLIENT", Stream.of("Client",
                    "editOwnAccountDetails",
                    "editOwnThemeSettings",
                    "editOwnAccountEmail",
                    "editOwnPassword",
                    "getOwnAccountInfo",
                    "logoutUser",
                    "refreshToken",
                    "addHotelRating",
                    "deleteHotelRating",
                    "updateHotelRating",
                    "getHotelRating",
                    "bookReservation",
                    "cancelReservation",
                    "getAllActiveReservations",
                    "getAllArchiveReservations",
                    "getEndedBookingsForHotel",
                    "getReservation",
                    "getAllCities",
                    "getAllBoxes",
                    "editOwnLanguage",
                    "getHotelForBooking").collect(Collectors.toSet()),
            "MANAGER", Stream.of("Manager",
                    "editOwnAccountDetails",
                    "editOwnThemeSettings",
                    "editOwnAccountEmail",
                    "editOwnPassword",
                    "getOwnAccountInfo",
                    "logoutUser",
                    "refreshToken",
                    "updateOwnHotel",
                    "generateReport",
                    "getHotelRating",
                    "cancelReservation",
                    "endReservation",
                    "startReservation",
                    "getAllActiveReservations",
                    "getAllArchiveReservations",
                    "getReservation",
                    "getAllCities",
                    "getCity",
                    "addBox",
                    "deleteBox",
                    "updateBox",
                    "getAllBoxes",
                    "editOwnLanguage",
                    "getOwnHotelInfo",
                    "getHotelForBooking").collect(Collectors.toSet()),
            "ADMIN", Stream.of("Admin",
                    "getHotelForBooking",
                    "getOtherHotelInfo",
                    "editOwnLanguage",
                    "getCity",
                    "getAllCities",
                    "updateCity",
                    "deleteCity",
                    "addCity",
                    "getHotelRating",
                    "hideHotelRating",
                    "deleteManagerFromHotel",
                    "addManagerToHotel",
                    "updateOtherHotel",
                    "deleteHotel",
                    "addHotel",
                    "getAllManagers",
                    "getManagerData",
                    "refreshToken",
                    "logoutUser",
                    "changeAccessLevel",
                    "getOtherAccountInfo",
                    "getOwnAccountInfo",
                    "getAllAccounts",
                    "editOtherPassword",
                    "editOwnPassword",
                    "unblockAccount",
                    "blockAccount",
                    "editOtherAccountEmail",
                    "editOwnAccountEmail",
                    "editOtherAccountDetails",
                    "editOwnThemeSettings",
                    "editOwnAccountDetails",
                    "deleteAccessLevel",
                    "addAccessLevel").collect(Collectors.toSet()));

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
            if (httpMessageContext.isProtected()) {
                return httpMessageContext.responseUnauthorized();
            }
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

                Set<String> stringSet = Arrays.stream(roles.split(","))
                        .map(x -> mapRoles.get(x))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());

                return httpMessageContext
                        .notifyContainerAboutLogin(login, stringSet);
            } catch (ParseException e) {
                return httpMessageContext.responseUnauthorized();
            }
        }
        return httpMessageContext.responseUnauthorized();
    }
}
