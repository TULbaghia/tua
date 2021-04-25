package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO reprezentująca rolę w systemie przypisaną do użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class RoleDto {
    private String login;
    private AccessLevel accessLevel;
    private boolean enabled;
}
