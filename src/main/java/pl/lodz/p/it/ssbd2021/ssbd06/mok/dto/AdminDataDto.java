package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO dziedzicząca po RoleDto, reprezentująca rolę Admin w systemie
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class AdminDataDto extends RoleDto{
    public AdminDataDto(String login, AccessLevel accessLevel, boolean enabled) {
        super(login, accessLevel, enabled);
    }
}