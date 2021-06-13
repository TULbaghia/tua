package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewCityDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy City na klasy DTO, oraz z obiektów klas DTO na obiekty klasy City
 */
@Mapper
public interface ICityMapper {
    /**
     * Dokonuje mapowania z obiektu City na obiekt CityDto
     *
     * @param city obiekt klasy City
     * @return zmapowany obiekt klasy CityDto
     */
    CityDto toCityDto(City city);

    /**
     * Dokonuje mapowania z obiektu NewCityDto na obiekt City.
     *
     * @param cityDto obiekt klasy NewCityDto.
     * @return zmapowany obiekt klasy City.
     */
    City toCity(NewCityDto cityDto);
}
