package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;

/**
 * Klasa DTO reprezentująca dane konieczne do rejestracji użytkownika
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegisterAccountDto {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;
}
