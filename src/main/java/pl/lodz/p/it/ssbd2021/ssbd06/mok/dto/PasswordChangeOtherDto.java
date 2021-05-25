package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Password;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentująca zmianę hasła innego użytkownika
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeOtherDto {
    @Login
    @NotNull
    private String login;
    @Password
    @NotNull
    @ToString.Exclude
    private String givenPassword;
}
