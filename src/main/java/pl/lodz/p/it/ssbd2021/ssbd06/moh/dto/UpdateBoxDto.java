package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.BoxDescription;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Klasa DTO służąca aktualizacji encji Box
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoxDto {
    @NotNull
    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    @BoxDescription
    private String description;
}
