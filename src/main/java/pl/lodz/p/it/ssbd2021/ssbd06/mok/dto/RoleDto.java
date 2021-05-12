package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

/**
 * Klasa DTO reprezentująca rolę w systemie przypisaną do użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class RoleDto implements Signable {
    private Long id;
    private AccessLevel accessLevel;
    private boolean enabled;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
