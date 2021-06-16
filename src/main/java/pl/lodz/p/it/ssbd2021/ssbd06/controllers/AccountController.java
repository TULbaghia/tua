package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.ThemeColor;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.RoleEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Language;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.PenCode;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.UserEmail;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Kontroler odpowiadający zasobom reprezentującym logikę obsługi kont.
 */
@Path("/accounts")
public class AccountController extends AbstractController {

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @Inject
    private RoleEndpointLocal roleEndpoint;

    @Inject
    private MessageSigner messageSigner;

    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @PUT
    @RolesAllowed("blockAccount")
    @EtagValidatorFilterBinding
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "blockAccount", summary = "blockAccount")
    public void blockAccount(@NotNull @Login @PathParam("login") @Valid String login)
            throws AppBaseException {
        repeat(() -> accountEndpoint.blockAccount(login), accountEndpoint);
    }

    /**
     * Odblokowywuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać odblokowane.
     * @throws AppBaseException gdy nie udało się odblokowanie konta.
     */
    @PUT
    @RolesAllowed("unblockAccount")
    @EtagValidatorFilterBinding
    @Path("/{login}/unblock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "unblockAccount", summary = "unblockAccount")
    public void unblockAccount(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        repeat(() -> accountEndpoint.unblockAccount(login), accountEndpoint);
    }


    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws AppBaseException gdy potwierdzenie konta się nie powiodło
     */
    @POST
    @Path("/confirm/{code}")
    @Operation(operationId = "confirm", summary = "confirm")
    public void confirm(@NotNull @PenCode @PathParam("code") @Valid String code) throws AppBaseException {
        repeat(() -> accountEndpoint.confirmAccount(code), accountEndpoint);
    }

    /**
     * Rejestruje konto użytkownika w systemie.
     *
     * @param registerAccountDto dane użytkownika do rejestracji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "registerAccount", summary = "registerAccount")
    public void registerAccount(@NotNull @Valid RegisterAccountDto registerAccountDto) throws AppBaseException {
        repeat(() -> accountEndpoint.registerAccount(registerAccountDto), accountEndpoint);
    }

    /**
     * Zmienia dane użytkownika wykonującego przypadek użycia w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @RolesAllowed("editOwnAccountDetails")
    @EtagValidatorFilterBinding
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "editOwnAccountDetails", summary = "editOwnAccountDetails")
    public void editOwnAccountDetails(@NotNull @Valid AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        repeat(() -> accountEndpoint.editOwnAccountDetails(accountPersonalDetailsDto), accountEndpoint);
    }

    /**
     * Zmienia dane wskazanego konta użytkownika w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param login                     login użytkownika, którego konto podlega modyfikacji.
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @RolesAllowed("editOtherAccountDetails")
    @EtagValidatorFilterBinding
    @Path("/edit/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "editOtherAccountDetails", summary = "editOtherAccountDetails")
    public void editOtherAccountDetails(@NotNull @Login @PathParam("login") @Valid String login,
                                        @NotNull @Valid AccountPersonalDetailsDto accountPersonalDetailsDto)
            throws AppBaseException {
        repeat(() -> accountEndpoint.editOtherAccountDetails(login, accountPersonalDetailsDto), accountEndpoint);
    }

    /**
     * Zwraca listę ról przypisanych do użytkownika.
     *
     * @param login login użytkownika
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @RolesAllowed("getOtherAccountInfo")
    @Path("/{login}/role")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getUserRole", summary = "getUserRole")
    public Response getUserRole(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        RolesDto rolesDto = repeat(() -> roleEndpoint.getUserRole(login), roleEndpoint);
        return Response.ok()
                .entity(rolesDto)
                .header("ETag", messageSigner.sign(rolesDto))
                .build();
    }

    /**
     * Zwraca listę ról zalogowanego użytkownika.
     *
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @RolesAllowed("getOwnAccountInfo")
    @Path("/self/role")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getSelfRole", summary = "getSelfRole")
    public Response getSelfRole() throws AppBaseException {
        RolesDto rolesDto = repeat(() -> roleEndpoint.getUserRole(), roleEndpoint);
        return Response.ok()
                .entity(rolesDto)
                .header("ETag", messageSigner.sign(rolesDto))
                .build();
    }

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param login       identyfikator użytkownika
     * @param accessLevel rola, która zostanie przydzielona użytkownikowi
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PATCH
    @RolesAllowed("addAccessLevel")
    @EtagValidatorFilterBinding
    @Path("/user/{login}/grant/{accessLevel}")
    @Operation(operationId = "grantAccessLevel", summary = "grantAccessLevel")
    public void grantAccessLevel(@NotNull @Login @PathParam("login") @Valid String login,
                                 @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        repeat(() -> roleEndpoint.grantAccessLevel(login, accessLevel), roleEndpoint);
    }

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param login       identyfikator użytkownika
     * @param accessLevel rola, która zostanie odebrana użytkownikowi
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PATCH
    @RolesAllowed("deleteAccessLevel")
    @EtagValidatorFilterBinding
    @Path("/user/{login}/revoke/{accessLevel}")
    @Operation(operationId = "revokeAccessLevel", summary = "revokeAccessLevel")
    public void revokeAccessLevel(@NotNull @Login @PathParam("login") @Valid String login,
                                  @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        repeat(() -> roleEndpoint.revokeAccessLevel(login, accessLevel), roleEndpoint);
    }

    /**
     * Zwraca listę wszystkich użytkowników systemu.
     *
     * @return lista użytkowników
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @RolesAllowed("getAllAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllAccountsList", summary = "getAllAccountsList")
    public List<AccountDto> getAllAccountsList() throws AppBaseException {
        return repeat(() -> accountEndpoint.getAllAccounts(), accountEndpoint);
    }

    /**
     * Zwraca dane konkretnego użytkownika
     *
     * @param login login użytkownika
     * @return dane konta wybranego użytkownika
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @RolesAllowed("getOtherAccountInfo")
    @Path("/user/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "showAccount", summary = "showAccount")
    public Response showAccount(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getAccount(login), accountEndpoint);
        return Response.ok()
                .entity(accountDto)
                .header("ETag", messageSigner.sign(accountDto))
                .build();
    }

    /**
     * Zwraca dane konta użytkownika, który wygenerował żądanie
     *
     * @return dane konta
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @RolesAllowed("getOwnAccountInfo")
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "showAccountInformation", summary = "showAccountInformation")
    public Response showAccountInformation() throws AppBaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getOwnAccountInfo(), accountEndpoint);
        return Response.ok()
                .entity(accountDto)
                .header("ETag", messageSigner.sign(accountDto))
                .build();
    }

    /**
     * Zmienia hasło użytkownika w systemie.
     *
     * @param passwordChangeDto dane użytkownika do rejestracji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @RolesAllowed("editOwnPassword")
    @EtagValidatorFilterBinding
    @Path("/self/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "changePassword", summary = "changePassword")
    public void changePassword(@NotNull @Valid PasswordChangeDto passwordChangeDto) throws AppBaseException {
        repeat(() -> accountEndpoint.changePassword(passwordChangeDto), accountEndpoint);
    }

    /**
     * Resetuje hasło użytkownika w systemie.
     *
     * @param passwordResetDto obiekt zawierający dane wymagane do resetowania hasła
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @POST
    @Path("/user/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "resetPassword", summary = "resetPassword")
    public void resetPassword(@NotNull @Valid PasswordResetDto passwordResetDto) throws AppBaseException {
        repeat(() -> accountEndpoint.resetPassword(passwordResetDto), accountEndpoint);
    }

    /**
     * Wysyła na istniejący w systemie email wiadomość o resetowaniu hasła.
     *
     * @param email email konta, na którego email zostanie wysłana wiadomość dotycząca resetowania
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @Path("/user/{email}/reset")
    @Operation(operationId = "sendResetPassword", summary = "sendResetPassword")
    public void sendResetPassword(@NotNull @UserEmail @PathParam("email") @Valid String email) throws AppBaseException {
        repeat(() -> accountEndpoint.sendResetPassword(email), accountEndpoint);
    }

    /**
     * Wysyła ponownie na istniejący w systemie email wiadomość o resetowaniu hasła.
     *
     * @param email email konta, na którego email zostanie wysłana wiadomość dotycząca resetowania
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @Path("/user/{email}/resetagain")
    @Operation(operationId = "sendResetPasswordAgain", summary = "sendResetPasswordAgain")
    public void sendResetPasswordAgain(@NotNull @UserEmail @PathParam("email") @Valid String email) throws AppBaseException {
        repeat(() -> accountEndpoint.sendResetPasswordAgain(email), accountEndpoint);
    }

    /**
     * Zmienia hasło innego użytkownika w systemie.
     *
     * @param passwordChangeOtherDto obiekt zawierający dane wymagane do zmiany hasła innego użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @RolesAllowed("editOtherPassword")
    @EtagValidatorFilterBinding
    @Path("/user/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "changeOtherPassword", summary = "changeOtherPassword")
    public void changeOtherPassword(@NotNull @Valid PasswordChangeOtherDto passwordChangeOtherDto) throws AppBaseException {
        repeat(() -> accountEndpoint.changeOtherPassword(passwordChangeOtherDto), accountEndpoint);
    }

    /**
     * Zmienia adres email wskazanego konta użytkownika.
     *
     * @param emailDto obiekt zawierający zmodyfikowany adres email oraz aktualny login użytkownika.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @RolesAllowed("editOwnAccountEmail")
    @EtagValidatorFilterBinding
    @Path("/self/edit/email")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "editOwnAccountEmail", summary = "editOwnAccountEmail")
    public void editOwnAccountEmail(@NotNull @Valid EmailDto emailDto) throws AppBaseException {
        repeat(() -> accountEndpoint.editOwnAccountEmail(emailDto), accountEndpoint);
    }

    /**
     * Zmienia adres email wskazanego konta użytkownika.
     *
     * @param emailDto obiekt zawierający zmodyfikowany adres email oraz aktualny login użytkownika.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @RolesAllowed("editOtherAccountEmail")
    @EtagValidatorFilterBinding
    @Path("/user/edit/email/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "editOtherAccountEmail", summary = "editOtherAccountEmail")
    public void editOtherAccountEmail(@NotNull @Login @PathParam("login") @Valid String login,
                                      @NotNull @Valid EmailDto emailDto) throws AppBaseException {
        repeat(() -> accountEndpoint.editOtherAccountEmail(emailDto, login), accountEndpoint);
    }

    /**
     * Przy użyciu podanego kodu aktywującego kończy procedurę zmiany adresu email przypisanego do konta.
     *
     * @param code żeton zmiany adresu email przypisanego do konta.
     * @throws AppBaseException proces zmiany adresu email przypisanego do konta zakończył się niepowodzeniem.
     */
    @POST
    @Path("/user/confirm/email/{code}")
    @Operation(operationId = "confirmEmail", summary = "confirmEmail")
    public void confirmEmail(@NotNull @PenCode @PathParam("code") @Valid String code) throws AppBaseException {
        repeat(() -> accountEndpoint.confirmEmail(code), accountEndpoint);
    }

    /**
     * Wywoływany w celu zapisania w dzienniku zdarzeń zmiany roli na frontend.
     *
     * @return kod odpowiedzi HTTP 200
     */
    @GET
    @RolesAllowed("changeAccessLevel")
    @Path("changeOwnAccessLevel/{accessLevel}")
    @Operation(operationId = "changeOwnAccessLevel", summary = "changeOwnAccessLevel")
    public Response changeOwnAccessLevel(@NotNull @PathParam("accessLevel") AccessLevel accessLevel) {
        accountEndpoint.changeOwnAccessLevel(accessLevel);
        return Response.ok().build();
    }

    /**
     * Edytuje język przypisany do konta użytkownika
     *
     * @param language nowy język, który ma być przypisany do konta
     * @throws AppBaseException proces zmiany języka zakończył się niepowodzeniem
     */
    @PATCH
    @EtagValidatorFilterBinding
    @RolesAllowed("editOwnLanguage")
    @Path("/self/edit/language/{lang}")
    @Operation(operationId = "editOwnLanguage", summary = "editOwnLanguage")
    public void editOwnLanguage(@PathParam("lang") @NotNull @Language String language) throws AppBaseException {
        repeat(() -> accountEndpoint.editOwnLanguage(language), accountEndpoint);
    }

    /**
     * Aktualizuje motyw interfejsu dla użytkownika.
     *
     * @param themeColor kolor interfejsu preferowany przez użytkownika
     * @throws AppBaseException proces utrwalenia zmiany motywu interfejsu zakonczył się niepowodzeniem
     */
    @PATCH
    @EtagValidatorFilterBinding
    @RolesAllowed("editOwnThemeSettings")
    @Path("/theme/{themeColor}")
    @Operation(operationId = "changeThemeColor", summary = "changeThemeColor")
    public void changeThemeColor(@NotNull @PathParam("themeColor") ThemeColor themeColor) throws AppBaseException {
        repeat(() -> accountEndpoint.changeThemeColor(themeColor), accountEndpoint);
    }

    /**
     * Zwraca listę wszystkich nieprzypisanych do hotelu managerów w systemie.
     *
     * @return lista użytkowników
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @RolesAllowed("getAllManagers")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/managers")
    @Operation(operationId = "getAllManagersList", summary = "getAllManagersList")
    public List<AccountManagerDto> getAllManagersList() throws AppBaseException {
        return repeat(() -> accountEndpoint.getAllManagers(), accountEndpoint);
    }

    /**
     * Zwraca przypisany do managera hotel.
     *
     * @param login login użytkownika
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @RolesAllowed("getManagerData")
    @Path("/{login}/manager")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getManagerData", summary = "getManagerData")
    public Response getManagerData(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        ManagerDataDto managerDataDto = repeat(() -> roleEndpoint.getManagerData(login), roleEndpoint);
        return Response.ok()
                .entity(managerDataDto)
                .header("ETag", messageSigner.sign(managerDataDto))
                .build();
    }
}
