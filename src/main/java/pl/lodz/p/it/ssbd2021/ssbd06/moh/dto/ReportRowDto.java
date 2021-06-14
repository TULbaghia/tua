package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;

import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca wiersz wygenerowanego raportu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRowDto {
    private Long id;
    private Long dateFrom;
    private Long dateTo;
    private String status;
    private BigDecimal price;
    private Short rating;
    private String ownerLogin;
}
