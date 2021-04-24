package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa DTO reprezentująca proces zmiany hasła użytkownika
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;
}
