package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.BoxDescription;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.ValueOfEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca klatkę
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxDto implements Signable {
    @NotNull
    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    @BoxDescription
    private String description;

    @ValueOfEnum(enumClass = AnimalType.class)
    private String animalType;

    @NotNull
    private Long hotelId;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%s", id, hotelId, animalType);
    }
}
