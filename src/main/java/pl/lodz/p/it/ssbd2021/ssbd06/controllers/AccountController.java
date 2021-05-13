package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountPersonalDetailsDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.security.SignatureValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.RoleEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.core.Response;

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
    public void changeAccountActiveStatus(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        repeat(()-> accountEndpoint.blockAccount(login), accountEndpoint);
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
    public void unblockAccount(@NotNull @PathParam("login") @Valid String login) throws AppBaseException {
        accountEndpoint.unblockAccount(login);
    }


    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws AppBaseException gdzy potwierdzenie konta się nie powiodło
     */
    @POST
    @Path("/confirm/{code}")
    public void confirm(@PathParam("code") String code) throws AppBaseException {
        repeat(()-> accountEndpoint.confirmAccount(code), accountEndpoint);
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
        repeat(()-> accountEndpoint.registerAccount(registerAccountDto), accountEndpoint);
    }

    /**
     * Zmienia dane użytkownika wykonującego przypadek użycia w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @SignatureValidatorFilterBinding
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editOwnAccountDetails(@NotNull @Valid AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        accountEndpoint.editOwnAccountDetails(accountPersonalDetailsDto);
    }

    /**
     * Zmienia dane wskazanego konta użytkownika w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param login login użytkownika, którego konto podlega modyfikacji.
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @SignatureValidatorFilterBinding
    @Path("/edit/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editOtherAccountDetails(@NotNull @PathParam("login") String login,
                                        @NotNull @Valid AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        accountEndpoint.editOtherAccountDetails(login, accountPersonalDetailsDto);
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
    public Response getUserRole(@NotNull @Login @PathParam("login") String login) throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole(login);
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
    public Response getUserRole() throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole();
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
    @Path("/{login}/grant/{accessLevel}")
    public void grantAccessLevel(@NotNull @Login @PathParam("login") String login, @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.grantAccessLevel(login, accessLevel);
    }

    /**
     * Odbiera uprawnienia użytkownikowi.
     *
     * @param login identyfikator użytkownika
     * @param accessLevel rola, która zostanie odebrana użytkownikowi
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PATCH
    @Path("/{login}/revoke/{accessLevel}")
    public void revokeAccessLevel(@NotNull @Login @PathParam("login") String login, @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.revokeAccessLevel(login, accessLevel);
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
        return accountEndpoint.getAllAccounts();
    }

    /**
     * Zwraca dane konkretnego użytkownika
     *
     * @param login login użytkownika
     * @return dane konta wybranego użytkownika
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAccount(@PathParam("login") String login) throws AppBaseException {
        AccountDto accountDto = accountEndpoint.getAccount(login);
        return Response.ok()
                .entity(accountDto)
                .tag(messageSigner.sign(accountDto))
                .build();
    }
}
