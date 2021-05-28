package pl.lodz.p.it.ssbd2021.ssbd06.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

import javax.validation.constraints.NotNull;

/**
 * DTO reprezentujący dane potrzebne do przeprowadzenia procesu logowania
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDataDto {
    @NotNull
    @Login
    private String login;

    @NotNull
    @ToString.Exclude
    private String password;
}
