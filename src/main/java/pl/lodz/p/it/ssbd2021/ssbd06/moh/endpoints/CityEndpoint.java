package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.CityDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.CityEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie miastami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CityEndpoint extends AbstractEndpoint implements CityEndpointLocal {
    @Override
    public CityDto get(Long id) throws AppBaseException {
        return null;
    }

    @Override
    public List<CityDto> getAll() throws AppBaseException {
        return null;
    }

    @Override
    public void addCity(CityDto cityDto) throws AppBaseException {

    }

    @Override
    public void updateCity(CityDto cityDto) throws AppBaseException {

    }

    @Override
    public void deleteCity(Long cityId) throws AppBaseException {

    }
}
