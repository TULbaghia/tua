package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Comment;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Rating;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezetnująca nową ocene
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRatingDto {
    @NotNull
    @Rating
    private Short rate;
    @Comment
    private String comment;
    @NotNull
    private Long bookingId;
}
