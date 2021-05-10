package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.ContactNumber;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Firstname;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Lastname;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

/**
 * Klasa DTO reprezentująca dane personalne użytkownika możliwe do edycji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPersonalDetailsDto {
    @Login
    private String login;
    @Firstname
    private String firstname;
    @Lastname
    private String lastname;
    @ContactNumber
    private String contactNumber;
}
