package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa DTO reprezetnująca nowy hotel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewHotelDto {
    private String name;
    private String address;
    private String cityName;
}
