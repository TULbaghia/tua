package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO potomna RoleDto, reprezentująca rolę Client w systemie
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ClientDataDto extends RoleDto {
    public ClientDataDto(Long id, AccessLevel accessLevel, boolean enabled, Long version) {
        super(id, accessLevel, enabled, version);
    }
}
