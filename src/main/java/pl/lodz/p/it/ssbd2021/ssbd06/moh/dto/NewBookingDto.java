package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Klasa DTO reprezentująca nową rezerwację
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingDto {
    List<MicroBoxes> boxes;

    private Date dateFrom;
    private Date dateTo;

    @Data
    static class MicroBoxes {
        private Long id;
        private Long number;
    }
}
