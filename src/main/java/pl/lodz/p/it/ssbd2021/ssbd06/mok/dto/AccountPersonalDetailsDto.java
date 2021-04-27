package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;

/**
 * Klasa DTO reprezentująca dane personalne użytkownika możliwe do edycji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPersonalDetailsDto {
    private String login;
    private String firstName;
    private String lastName;
    private String contactNumber;
}
