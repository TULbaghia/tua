package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.CityDescription;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.CityName;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentujaca tworzenie nowego miasta.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCityDto {
    @NotNull
    @CityName
    private String name;

    @NotNull
    @CityDescription
    private String description;
}
