package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Password;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentująca zmianę hasła użytkownika
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
    @Password
    @NotNull
    @ToString.Exclude
    private String oldPassword;
    @Password
    @NotNull
    @ToString.Exclude
    private String newPassword;
}
