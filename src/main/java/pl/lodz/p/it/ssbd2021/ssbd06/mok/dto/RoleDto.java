package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO reprezentująca rolę w systemie przypisaną do użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDto {
    private String login;
    private AccessLevel accessLevel;
    private boolean enabled;

    public RoleDto(Account account, AccessLevel accessLevel, boolean enabled){
        this.login = account.getLogin();
        this.accessLevel = accessLevel;
        this.enabled = enabled;
    }
}
