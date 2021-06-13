package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.ICityMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewCityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.CityEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.CityManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint odpowiadający za zarządzanie miastami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CityEndpoint extends AbstractEndpoint implements CityEndpointLocal {

    @Inject
    private CityManager cityManager;

    @Override
    public CityDto get(Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("getAllCities")
    public List<CityDto> getAllCities() throws AppBaseException {
        ICityMapper cityMapper = Mappers.getMapper(ICityMapper.class);
        List<City> allCities = cityManager.getAll();
        return allCities.stream().map(cityMapper::toCityDto).collect(Collectors.toList());
    }

    @Override
    @RolesAllowed("addCity")
    public void addCity(NewCityDto newCityDto) throws AppBaseException {
        ICityMapper cityMapper = Mappers.getMapper(ICityMapper.class);
        cityManager.addCity(cityMapper.toCity(newCityDto));
    }

    @Override
    @RolesAllowed("updateCity")
    public void updateCity(CityDto cityDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("deleteCity")
    public void deleteCity(Long cityId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
