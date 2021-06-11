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
    List<BookedBoxes> boxes;

    private Date dateFrom;
    private Date dateTo;
    private Long hotelId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookedBoxes {
        private int type;
        private long quantity;
    }
}
