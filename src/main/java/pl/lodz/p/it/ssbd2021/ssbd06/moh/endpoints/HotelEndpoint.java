package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.HotelEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie hotelami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HotelEndpoint extends AbstractEndpoint implements HotelEndpointLocal {

    @Override
    public HotelDto get(Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HotelDto> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public HotelDto lookForHotel(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HotelDto> getAllFilter(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateHotel(UpdateHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteHotel(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteManagerFromHotel(String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenerateReportDto generateReport(Long hotelId, String from, String to) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
