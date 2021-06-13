package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa DTO reprezentująca wygenerowany raport.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateReportDto {
    private int count;
    private List<ReportRowDto> bookings;
}
