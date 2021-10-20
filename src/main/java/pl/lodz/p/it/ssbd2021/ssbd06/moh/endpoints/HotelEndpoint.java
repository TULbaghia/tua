package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IBookingMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IHotelMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.HotelEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.AccountManagerMoh;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.CityManager;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.HotelManager;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.RoleManagerMoh;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Resource(name = "sessionContext")
    SessionContext sessionContext;

    @Inject
    private AccountManagerMoh accountManagerMoh;

    @Inject
    private RoleManagerMoh roleManagerMoh;

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
        for (Hotel hotel : hotels) {
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
                .filter(hotel -> hotelManager.checkHotelNameContainsString(hotel, searchQuery))
                .collect(Collectors.toList());
        List<HotelDto> result = new ArrayList<>();
        for (Hotel hotel : hotels) {
            HotelDto hotelDto = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
            hotelDto.setCityName(hotel.getCity().getName());
            result.add(hotelDto);
        }
        return result;
    }

    @Override
    @PermitAll
    public List<HotelDto> getAllFilter(BigDecimal fromRating,
                                       BigDecimal toRating,
                                       boolean dog,
                                       boolean cat,
                                       boolean rodent,
                                       boolean bird,
                                       boolean rabbit,
                                       boolean lizard,
                                       boolean turtle,
                                       String searchQuery) throws AppBaseException {
        List<AnimalType> animalTypes = new ArrayList<>();
        if (dog) animalTypes.add(AnimalType.DOG);
        if (cat) animalTypes.add(AnimalType.CAT);
        if (rodent) animalTypes.add(AnimalType.RODENT);
        if (bird) animalTypes.add(AnimalType.BIRD);
        if (rabbit) animalTypes.add(AnimalType.RABBIT);
        if (lizard) animalTypes.add(AnimalType.LIZARD);
        if (turtle) animalTypes.add(AnimalType.TURTLE);
        return hotelManager.getAllFilter(fromRating, toRating, animalTypes, searchQuery);
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
        Account account = accountManagerMoh.findByLogin(managerLogin);
        Set<Role> roleList = account.getRoleList();
        Role managerRole = null;
        for (Role role : roleList) {
            if (role.getAccessLevel() == AccessLevel.MANAGER && role.isEnabled()) {
                managerRole = role;
            }
        }
        ManagerData managerData = roleManagerMoh.find(managerRole.getId());
        ManagerDataDto managerDataDto = Mappers.getMapper(IRoleMapper.class).toManagerDataDto(managerData);
        if (!verifyIntegrity(managerDataDto)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        hotelManager.addManagerToHotel(hotelId, managerData);
    }

    @Override
    @RolesAllowed("deleteManagerFromHotel")
    public void deleteManagerFromHotel(String managerLogin) throws AppBaseException {
        var managerData = hotelManager.findHotelByManagerLogin(managerLogin).getManagerDataList()
                .stream()
                .filter(x -> x.getAccount().getLogin().equals(managerLogin))
                .findAny()
                .get();
        ManagerDataDto managerDataDto = Mappers.getMapper(IRoleMapper.class).toManagerDataDto(managerData);

        if (!verifyIntegrity(managerDataDto)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        hotelManager.deleteManagerFromHotel(managerLogin);
    }

    @Override
    @RolesAllowed("updateOwnHotel")
    public void updateOwnHotel(UpdateHotelDto hotelDto) throws AppBaseException {
        String managerLogin = sessionContext.getCallerPrincipal().getName();
        Hotel hotel = hotelManager.findHotelByManagerLogin(managerLogin);

        HotelDto hotelIntegrity = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
        if (!verifyIntegrity(hotelIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        Mappers.getMapper(IHotelMapper.class).toHotel(hotelDto, hotel);
        City city = cityManager.get(hotelDto.getCityId());
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
        City city = cityManager.get(hotelDto.getCityId());
        hotel.setCity(city);
        hotelManager.updateHotel(hotel);
    }

    @Override
    @RolesAllowed("getOwnHotelInfo")
    public HotelDto getOwnHotelInfo() throws AppBaseException {
        String managerLogin = sessionContext.getCallerPrincipal().getName();
        Hotel hotel = hotelManager.findHotelByManagerLogin(managerLogin);
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
    }

    @Override
    @RolesAllowed("getOtherHotelInfo")
    public HotelDto getOtherHotelInfo(Long id) throws AppBaseException {
        Hotel hotel = hotelManager.findHotelById(id);
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
    }

    @Override
    @RolesAllowed("getHotelForBooking")
    public HotelDto getHotelForBooking(Long id) throws AppBaseException {
        Hotel hotel = hotelManager.getHotelForBooking(id);
        return Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
    }

    @Override
    @RolesAllowed("generateReport")
    public GenerateReportDto generateReport(Long from, Long to) throws AppBaseException {
        IBookingMapper im = Mappers.getMapper(IBookingMapper.class);
        String managerLogin = sessionContext.getCallerPrincipal().getName();
        Hotel hotel = hotelManager.findHotelByManagerLogin(managerLogin);

        List<ReportRowDto> reportContent = new ArrayList<>();
        hotelManager.generateReport(hotel, new Date(from * 1000), new Date(to * 1000))
                .forEach(x -> reportContent.add(im.toReportRowDto(x)));

        return GenerateReportDto
                .builder()
                .bookings(reportContent)
                .count(reportContent.size())
                .build();
    }
}
