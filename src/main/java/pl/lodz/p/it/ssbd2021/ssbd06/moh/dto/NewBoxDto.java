package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.BoxDescription;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.ValueOfEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca nową klatkę
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBoxDto {
    @NotNull
    private BigDecimal price;
    @ValueOfEnum(enumClass = AnimalType.class)
    private String animalType;
    @BoxDescription
    private String description;
}
