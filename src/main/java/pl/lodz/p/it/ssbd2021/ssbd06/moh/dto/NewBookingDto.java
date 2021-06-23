package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Klasa DTO reprezentująca nową rezerwację
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingDto {
    @NotNull
    private Date dateFrom;
    @NotNull
    private Date dateTo;
    @NotNull
    private Long hotelId;
    @NotEmpty
    List<Long> boxes;
}
