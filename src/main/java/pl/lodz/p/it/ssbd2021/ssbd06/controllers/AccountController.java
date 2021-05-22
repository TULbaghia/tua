package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.RoleEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.PenCode;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.UserEmail;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Path("/{login}/unblock")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @EtagValidatorFilterBinding
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @EtagValidatorFilterBinding
    @Path("/edit/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Path("/{login}/role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRole(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        RolesDto rolesDto = repeat(() -> roleEndpoint.getUserRole(login), roleEndpoint);
        return Response.ok()
                .entity(rolesDto)
                .tag(messageSigner.sign(rolesDto))
                .build();
    }

    /**
     * Zwraca listę ról zalogowanego użytkownika.
     *
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @Path("/self/role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSelfRole() throws AppBaseException {
        RolesDto rolesDto = repeat(() -> roleEndpoint.getUserRole(), roleEndpoint);
        return Response.ok()
                .entity(rolesDto)
                .tag(messageSigner.sign(rolesDto))
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
    @EtagValidatorFilterBinding
    @Path("/user/{login}/grant/{accessLevel}")
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
    @EtagValidatorFilterBinding
    @Path("/user/{login}/revoke/{accessLevel}")
    public void revokeAccessLevel(@NotNull @Login @PathParam("login") @Valid String login,
                                  @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        repeat(() -> roleEndpoint.revokeAccessLevel(login, accessLevel), roleEndpoint);
    }

    /**
     * Zwraca listę wszystkich użytkowników systemu reprezentowanych jako DTO.
     *
     * @return lista użytkowników w formie DTO
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
    @Path("/user/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAccount(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getAccount(login), accountEndpoint);
        return Response.ok()
                .entity(accountDto)
                .tag(messageSigner.sign(accountDto))
                .build();
    }

    /**
     * Zwraca dane konta użytkownika, który wygenerował żądanie
     *
     * @return dane konta
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAccountInformation() throws AppBaseException {
        AccountDto accountDto = repeat(() -> accountEndpoint.getOwnAccountInfo(), accountEndpoint);
        return Response.ok()
                .entity(accountDto)
                .tag(messageSigner.sign(accountDto))
                .build();
    }

    /**
     * Zmienia hasło użytkownika w systemie.
     *
     * @param passwordChangeDto dane użytkownika do rejestracji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @EtagValidatorFilterBinding
    @Path("/self/password")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public void sendResetPassword(@NotNull @UserEmail @PathParam("email") @Valid String email) throws AppBaseException {
        repeat(() -> accountEndpoint.sendResetPassword(email), accountEndpoint);
    }

    /**
     * Wysyła ponownie na istniejący w systemie email wiadomość o resetowaniu hasła.
     *
     * @param login login konta, na którego email zostanie wysłana wiadomość dotycząca resetowania
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @Path("/user/{login}/resetagain")
    public void sendResetPasswordAgain(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        repeat(() -> accountEndpoint.sendResetPasswordAgain(login), accountEndpoint);
    }

    /**
     * Zmienia hasło innego użytkownika w systemie.
     *
     * @param passwordChangeOtherDto obiekt zawierający dane wymagane do zmiany hasła innego użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @Path("/user/password")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @EtagValidatorFilterBinding
    @Path("/self/edit/email")
    @Consumes(MediaType.APPLICATION_JSON)
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
    @EtagValidatorFilterBinding
    @Path("/user/edit/email/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public void confirmEmail(@NotNull @PenCode @PathParam("code") @Valid String code) throws AppBaseException {
        repeat(() -> accountEndpoint.confirmEmail(code), accountEndpoint);
    }
}
