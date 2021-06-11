package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;

@Mapper
public interface ICityMapper {

    CityDto toCityDto(City city);
}
