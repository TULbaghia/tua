package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.ValueOfEnum;

import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca nową klatkę
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBoxDto {
    private BigDecimal price;
    @ValueOfEnum(enumClass = AnimalType.class)
    private String animalType;
}
