package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO dziedzicząca po RoleDto, reprezentująca rolę Manager w systemie
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ManagerDataDto extends RoleDto{
    private String hotelName;

    public ManagerDataDto(String login, AccessLevel accessLevel, boolean enabled, String hotelName) {
        super(login, accessLevel, enabled);
        this.hotelName = hotelName;
    }
}
