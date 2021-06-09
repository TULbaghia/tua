package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IHotelMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.HotelEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.CityManager;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.HotelManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.SecurityContext;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie hotelami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HotelEndpoint extends AbstractEndpoint implements HotelEndpointLocal {

    @Inject
    private HotelManager hotelManager;

    @Inject
    private CityManager cityManager;

    @Inject
    private SecurityContext securityContext;

    @Override
    @PermitAll
    public HotelDto get(Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @PermitAll
    public List<HotelDto> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @PermitAll
    public HotelDto lookForHotel(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @PermitAll
    public List<HotelDto> getAllFilter(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("addHotel")
    public void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("deleteHotel")
    public void deleteHotel(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("addManagerToHotel")
    public void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("deleteManagerFromHotel")
    public void deleteManagerFromHotel(String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("generateReport")
    public GenerateReportDto generateReport(Long hotelId, String from, String to) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("updateOwnHotel")
    public void updateOwnHotel(UpdateHotelDto hotelDto) throws AppBaseException {
        String managerLogin = securityContext.getCallerPrincipal().getName();
        Hotel hotel = hotelManager.findHotelByManagerLogin(managerLogin);

        HotelDto hotelIntegrity = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
        if (!verifyIntegrity(hotelIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        Mappers.getMapper(IHotelMapper.class).toHotel(hotelDto, hotel);
        City city = cityManager.findByName(hotelDto.getCityName());
        hotel.setCity(city);
        hotelManager.updateHotel(hotel);
    }

    @Override
    @RolesAllowed("updateOtherHotel")
    public void updateOtherHotel(Long id, UpdateHotelDto hotelDto) throws AppBaseException {
        Hotel hotel = hotelManager.findHotelById(id);

        HotelDto hotelIntegrity = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
        if (!verifyIntegrity(hotelIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        Mappers.getMapper(IHotelMapper.class).toHotel(hotelDto, hotel);
        City city = cityManager.findByName(hotelDto.getCityName());
        hotel.setCity(city);
        hotelManager.updateHotel(hotel);
    }
}
