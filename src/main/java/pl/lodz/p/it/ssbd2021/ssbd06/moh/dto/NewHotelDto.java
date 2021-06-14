package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.HotelAddress;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.HotelDescription;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.HotelImage;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.HotelName;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezetnująca nowy hotel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewHotelDto {
    @NotNull
    @HotelName
    private String name;

    @NotNull
    @HotelAddress
    private String address;

    @HotelImage
    private String image;

    @HotelDescription
    private String description;

    @NotNull
    private Long cityId;
}
