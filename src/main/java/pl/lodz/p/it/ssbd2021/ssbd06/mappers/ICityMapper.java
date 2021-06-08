package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;

public interface ICityMapper {

    CityDto toCityDto(City city);
}
