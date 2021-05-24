package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Password;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.PenCode;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentująca resetowanie hasła użytkownika
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {
    @Password
    @NotNull
    @ToString.Exclude
    private String password;
    @PenCode
    @NotNull
    private String resetCode;
}
