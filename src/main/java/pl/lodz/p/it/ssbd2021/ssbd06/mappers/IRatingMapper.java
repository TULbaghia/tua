package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Rating na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Rating
 */
@Mapper
public interface IRatingMapper {

    /**
     * Dokonuje mapowania z obiektu RatingDto na obiekt Rating
     *
     * @param ratingDto obiekt klasy RatingDto
     * @return obiekt klasy Rating na ktróry przeprowadzamy mapowanie
     */
    Rating toRating(RatingDto ratingDto);
}
