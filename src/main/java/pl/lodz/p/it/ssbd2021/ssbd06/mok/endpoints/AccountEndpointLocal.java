package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;


import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.ThemeColor;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Wykonuje konwersje klas DTO na model biznesowy
 */
@Local
public interface AccountEndpointLocal extends CallingClass {
    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @RolesAllowed("blockAccount")
    void blockAccount(String login) throws AppBaseException;

    /**
     * Odblokowywuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać odblokowane.
     * @throws AppBaseException gdy nie udało się odblokowanie konta.
     */
    @RolesAllowed("unblockAccount")
    void unblockAccount(String login) throws AppBaseException;

    /**
     * Rejestruje konto użytkownika.
     *
     * @param registerAccountDto obiekt zawierający dane wymagane do utworzenia konta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException;

    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     */
    @PermitAll
    void confirmAccount(String code) throws AppBaseException;

    /**
     * Aktualizuje dane związane z niepoprawnym uwierzytelnieniem się użytkownika.
     *
     * @param login     login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate  data udanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    void updateValidAuth(String login, String ipAddress, Date authDate) throws AppBaseException;

    /**
     * Aktualizuje dane związane z poprawnym uwierzytelnieniem się użytkownika.
     *
     * @param login     login użytkownika
     * @param ipAddress adres ip użytkownika
     * @param authDate  data nieudanego uwierzytelnienia
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    void updateInvalidAuth(String login, String ipAddress, Date authDate) throws AppBaseException;

    /**
     * Zmienia dane użytkownika wykonującego przypadek użycia w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed("editOwnAccountDetails")
    void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException;

    /**
     * Zmienia dane wskazanego konta użytkownika użytkownika w zakresie: imienia, nazwiska oraz numeru kontaktowego.
     *
     * @param login                     login użytkownika, którego konto podlega modyfikacji.
     * @param accountPersonalDetailsDto obiekt konta zmodyfikowany w dostępnym zakresie.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed("editOtherAccountDetails")
    void editOtherAccountDetails(String login, AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException;

    /**
     * Zwraca listę wszystkich użytkowników systemu reprezentowanych jako DTO.
     *
     * @return lista użytkowników w formie DTO
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getAllAccounts")
    List<AccountDto> getAllAccounts() throws AppBaseException;

    /**
     * Zwraca dane konkretnego użytkownika
     *
     * @param login login użytkownika
     * @return dane konta wybranego użytkownika
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOtherAccountInfo")
    AccountDto getAccount(String login) throws AppBaseException;

    /**
     * Zwraca dane konta użytkownika, który wygenerował żądanie
     *
     * @return dane konta
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOwnAccountInfo")
    AccountDto getOwnAccountInfo() throws AppBaseException;

    /**
     * Aktualizuje dane związane ze zmianą hasła przez użytkownika.
     *
     * @param passwordChangeDto obiekt zawierający dane wymagane do zmiany hasła
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("editOwnPassword")
    void changePassword(PasswordChangeDto passwordChangeDto) throws AppBaseException;

    /**
     * Resetuje hasło użytkownika.
     *
     * @param passwordResetDto obiekt zawierający dane wymagane do resetowania hasła
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    void resetPassword(PasswordResetDto passwordResetDto) throws AppBaseException;

    /**
     * Wysyła token resetujący hasło użytkownika o podanym emailu.
     *
     * @param email email konta, na którego email zostanie wysłana wiadomość dotycząca resetowania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    void sendResetPassword(String email) throws AppBaseException;

    /**
     * Wysyła ponownie token resetujący hasło użytkownika o podanym emailu.
     *
     * @param login login konta, na którego email zostanie wysłana wiadomość dotycząca resetowania
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @PermitAll
    void sendResetPasswordAgain(String login) throws AppBaseException;

    /**
     * Zmienia hasło innego użytkownika.
     *
     * @param passwordChangeOtherDto obiekt zawierający dane wymagane do zmiany hasła
     * @throws AppBaseException gdy nie udało się zaktualizować danych
     */
    @RolesAllowed("editOtherPassword")
    void changeOtherPassword(PasswordChangeOtherDto passwordChangeOtherDto) throws AppBaseException;

    /**
     * Zmienia adres email wskazanego konta użytkownika.
     *
     * @param emailDto obiekt zawierający zmodyfikowany adres email oraz aktualny login użytkownika.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed("editOwnAccountEmail")
    void editOwnAccountEmail(EmailDto emailDto) throws AppBaseException;

    /**
     * Zmienia adres email wskazanego konta użytkownika.
     *
     * @param emailDto obiekt zawierający zmodyfikowany adres email oraz aktualny login użytkownika.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed("editOtherAccountEmail")
    void editOtherAccountEmail(EmailDto emailDto, String login) throws AppBaseException;

    /**
     * Przy użyciu podanego kodu aktywującego kończy procedurę zmiany adresu email przypisanego do konta.
     *
     * @param code żeton zmiany adresu email przypisanego do konta.
     * @throws AppBaseException proces zmiany adresu email przypisanego do konta zakończył się niepowodzeniem.
     */
    @PermitAll
    void confirmEmail(String code) throws AppBaseException;

    /**
     * Zapisuje w dzienniku zdarzeń fakt zmiany poziomu dostępu przez użytkownika
     */
    @RolesAllowed("changeAccessLevel")
    void changeOwnAccessLevel(AccessLevel accessLevel);

    /**
     * Wysyła email na konto użytkownika z poziomem administracyjnym po zalogowaniu.
     * Reprezentuje powiadomienie o zalogowaniu na konto administratora.
     *
     * @param address    adres logiczny z jakiego nastąpiło logowanie na konto z administracyjnym poziomem dostępu.
     * @param adminLogin login konta administratora.
     * @throws AppBaseException proces wysyłania powiadomienia zakończył się niepowodzeniem.
     */
    @PermitAll
    void sendAdminLoginInfo(String adminLogin, String address) throws AppBaseException;

    /**
     * Edytuje język przypisany do konta użytkownika
     *
     * @param language nowy język, który ma być przypisany do konta
     * @throws AppBaseException proces zmiany języka zakończył się niepowodzeniem
     */
    @RolesAllowed("editOwnLanguage")
    void editOwnLanguage(String language) throws AppBaseException;

    /**
     * Aktualizuje motyw interfejsu dla użytkownika.
     *
     * @param themeColor kolor interfejsu preferowany przez użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void changeThemeColor(ThemeColor themeColor) throws AppBaseException;
}
