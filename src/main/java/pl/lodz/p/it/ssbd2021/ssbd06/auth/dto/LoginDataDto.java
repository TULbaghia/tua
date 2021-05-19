package pl.lodz.p.it.ssbd2021.ssbd06.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDataDto {
    @NotNull
    @Login
    private String login;
    @NotNull
    private String password;
}
