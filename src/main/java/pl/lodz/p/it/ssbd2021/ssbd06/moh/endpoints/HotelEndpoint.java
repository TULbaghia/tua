package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Role;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IHotelMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.HotelEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.HotelManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.RoleManager;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.CityManager;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
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
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

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

    @Inject
    private AccountManager accountManager;

    @Inject
    private RoleManager roleManager;

    @Override
    @PermitAll
    public HotelDto get(Long id) throws AppBaseException {
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotelManager.get(id));
    }

    @Override
    @PermitAll
    public List<HotelDto> getAll() throws AppBaseException {
        List<Hotel> hotels = hotelManager.getAll();
        List<HotelDto> result = new ArrayList<>();
        for (Hotel hotel: hotels) {
            HotelDto hotelDto = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
            hotelDto.setCityName(hotel.getCity().getName());
            result.add(hotelDto);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<HotelDto> lookForHotel(String searchQuery) throws AppBaseException {
        List<Hotel> hotels = hotelManager.getAll()
                .stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(searchQuery.toLowerCase())
                || hotel.getAddress().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
        List<HotelDto> result = new ArrayList<>();
        for (Hotel hotel: hotels) {
            HotelDto hotelDto = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
            hotelDto.setCityName(hotel.getCity().getName());
            result.add(hotelDto);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<HotelDto> getAllFilter(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("addHotel")
    public void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        hotelManager.addHotel(hotelDto);
    }

    @Override
    @RolesAllowed("deleteHotel")
    public void deleteHotel(Long hotelId) throws AppBaseException {
        Hotel hotel = hotelManager.get(hotelId);
        HotelDto hotelIntegrity = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
        if (!verifyIntegrity(hotelIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        hotelManager.deleteHotel(hotelId);
    }

    @Override
    @RolesAllowed("addManagerToHotel")
    public void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException {
        Account account = accountManager.findByLogin(managerLogin);
        Set<Role> roleList = account.getRoleList();
        Role managerRole = null;
        for (Role role: roleList) {
            if (role.getAccessLevel() == AccessLevel.MANAGER && role.isEnabled()) {
                managerRole = role;
            }
        }
        ManagerData managerData = roleManager.find(managerRole.getId());
        ManagerDataDto managerDataDto = Mappers.getMapper(IRoleMapper.class).toManagerDataDto(managerData);
        if (!verifyIntegrity(managerDataDto)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        hotelManager.addManagerToHotel(hotelId, managerLogin);
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

    @Override
    @RolesAllowed("getOwnHotelInfo")
    public HotelDto getOwnHotelInfo() throws AppBaseException {
        String managerLogin = securityContext.getCallerPrincipal().getName();
        Hotel hotel = hotelManager.findHotelByManagerLogin(managerLogin);
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
    }

    @Override
    @RolesAllowed("getOtherHotelInfo")
    public HotelDto getOtherHotelInfo(Long id) throws AppBaseException {
        Hotel hotel = hotelManager.findHotelById(id);
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
    }
}
