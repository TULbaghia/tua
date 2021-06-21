package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Comment;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Rating;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRatingDto {

    @NotNull
    private Long id;

    @NotNull
    @Rating
    private short rate;
    
    @Comment
    private String comment;
}
