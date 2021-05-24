package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca klatkę
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxDto {
    private Long id;
    private BigDecimal price;
    private String animalType;
    private Long hotelId;
}
