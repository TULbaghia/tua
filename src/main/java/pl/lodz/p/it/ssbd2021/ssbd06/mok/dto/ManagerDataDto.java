package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;

/**
 * Klasa DTO dziedzicząca po RoleDto, reprezentująca rolę Manager w systemie
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerDataDto extends RoleDto{
    private String hotelName;
}
