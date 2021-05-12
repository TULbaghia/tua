package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.*;

/**
 * Klasa DTO reprezentująca dane konieczne do rejestracji użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterAccountDto {
    @Login
    private String login;
    @Password
    private String password;
    @Firstname
    private String firstname;
    @Lastname
    private String lastname;
    @ContactNumber
    private String contactNumber;
}
