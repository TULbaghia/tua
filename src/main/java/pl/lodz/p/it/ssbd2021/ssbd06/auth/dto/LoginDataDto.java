package pl.lodz.p.it.ssbd2021.ssbd06.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDataDto {
    @NotNull
    private String login;
    @NotNull
    private String password;
}
