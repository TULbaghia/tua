package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Password;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentująca proces resetowania hasła użytkownika
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {
    @Password
    @NotNull
    private String password;
    @NotNull
    private PendingCode resetCode;
}
