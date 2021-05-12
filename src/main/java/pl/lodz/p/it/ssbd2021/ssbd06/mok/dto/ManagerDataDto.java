package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

/**
 * Klasa DTO dziedzicząca po RoleDto, reprezentująca rolę Manager w systemie
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ManagerDataDto extends RoleDto {
    private String hotelName;

    public ManagerDataDto(Long id, AccessLevel accessLevel, boolean enabled, Long version, String hotelName) {
        super(id, accessLevel, enabled, version);
        this.hotelName = hotelName;
    }
}
