package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Rating na klasy DTO, oraz z obiektów klas DTO na obiekty
 * klasy Rating
 */
@Mapper
public interface IRatingMapper {

    /**
     * Dokonuje mapowania z obiektu Rating na obiekt RatingDto
     *
     * @param rating obiekt klasy Rating
     * @return zmapowany obiekt klasy RatingDto
     */
    @Mapping(target = "createdBy", expression = "java(rating.getCreatedBy().getLogin())")
    @Mapping(target = "bookingId", expression = "java(rating.getBooking().getId())")
    @Mapping(target = "comment", expression = "java(rating.isHidden() ? null : rating.getComment())")
    RatingDto toRatingDto(Rating rating);

    /**
     * Dokonuje mapowania z obiektu RatingDto na obiekt Rating
     *
     * @param ratingDto obiekt klasy RatingDto
     * @return obiekt klasy Rating na który przeprowadzamy mapowanie
     */
    @Mapping(target = "createdBy", ignore = true)
    Rating toRating(RatingDto ratingDto);
}