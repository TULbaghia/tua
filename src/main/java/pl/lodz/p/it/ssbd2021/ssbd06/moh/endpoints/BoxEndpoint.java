package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Role;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.BoxException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IBoxMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BoxEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.BoxManager;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie klatkami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BoxEndpoint extends AbstractEndpoint implements BoxEndpointLocal {

    @Inject
    private BoxManager boxManager;

    @Inject
    private HotelManager hotelManager;

    @Override
    public BoxDto get(Long id) throws AppBaseException {
        return Mappers.getMapper(IBoxMapper.class).toBoxDto(boxManager.get(id));
    }

    @Override
    @RolesAllowed("getAllBoxes")
    public List<BoxDto> getAll() throws AppBaseException {
        List<Box> boxes = boxManager.getAll();
        List<BoxDto> result = new ArrayList<>();
        for(Box box : boxes) {
            result.add(Mappers.getMapper(IBoxMapper.class).toBoxDto(box));
        }
        return result;
    }

    @Override
    @RolesAllowed("getAllBoxes")
    public List<BoxDto> getAllBoxesInHotel(String loginManger) throws AppBaseException {
        List<Box> boxes = boxManager.getAll();
        List<BoxDto> result = new ArrayList<>();
        for(Box box : boxes) {
            if(box.getHotel().getId().equals(hotelManager.findHotelByManagerLogin(loginManger).getId())) {
                result.add(Mappers.getMapper(IBoxMapper.class).toBoxDto(box));
            }
        }
        return result;
    }

    @Override
    @RolesAllowed("getAllBoxes")
    public List<BoxDto> getAllBoxesInHotel(Long hotelId) throws AppBaseException {
        List<Box> boxes = boxManager.getAll();
        List<BoxDto> result = new ArrayList<>();
        for(Box box : boxes) {
            if(box.getHotel().getId().equals(hotelId)) {
                result.add(Mappers.getMapper(IBoxMapper.class).toBoxDto(box));
            }
        }
        return result;
    }

    @Override
    @RolesAllowed("getAllBoxes")
    public List<BoxDto> getSomeTypeBoxesFromHotel(Long hotelId, AnimalType animalType) throws AppBaseException {
        List<Box> boxes = boxManager.getAll();
        List<BoxDto> result = new ArrayList<>();
        for(Box box : boxes) {
            if(box.getHotel().getId().equals(hotelId) && box.getAnimalType().equals(animalType)) {
                result.add(Mappers.getMapper(IBoxMapper.class).toBoxDto(box));
            }
        }
        return result;
    }


    @Override
    @RolesAllowed("addBox")
    public void addBox(NewBoxDto boxDto) throws AppBaseException {
        Box box = new Box();
        Mappers.getMapper(IBoxMapper.class).toBox(boxDto, box);
        boxManager.addBox(box);
    }

    @Override
    @RolesAllowed("updateBox")
    public void updateBox(UpdateBoxDto boxDto) throws AppBaseException {
        Box box = boxManager.get(boxDto.getId());

        BoxDto boxIntegrity = Mappers.getMapper(IBoxMapper.class).toBoxDto(box);
        if(!verifyIntegrity(boxIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        Mappers.getMapper(IBoxMapper.class).toBox(boxDto, box);
        boolean canModify = box.getHotel().getManagerDataList()
                .stream()
                .filter(Role::isEnabled).anyMatch(x -> x.getAccount().getLogin().equals(getLogin()));
        if(canModify) {
            boxManager.updateBox(box);
        } else {
            throw BoxException.accessDenied();
        }
    }

    @Override
    @RolesAllowed("deleteBox")
    public void deleteBox(Long boxId) throws AppBaseException {
        Box boxToDelete = boxManager.get(boxId);

        BoxDto boxIntegrity = Mappers.getMapper(IBoxMapper.class).toBoxDto(boxToDelete);
        if(!verifyIntegrity(boxIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        boolean canDelete = boxToDelete.getHotel().getManagerDataList()
                .stream()
                .filter(Role::isEnabled).anyMatch(x -> x.getAccount().getLogin().equals(getLogin()));

        boolean isInProgress = boxToDelete.getBookingLineList()
                .stream()
                .map(BookingLine::getBooking)
                .anyMatch(x -> x.getStatus().equals(BookingStatus.IN_PROGRESS));

        boolean isPending = boxToDelete.getBookingLineList()
                .stream()
                .map(BookingLine::getBooking)
                .anyMatch(x -> x.getStatus().equals(BookingStatus.PENDING));

        if(!canDelete) {
            throw BoxException.accessDenied();
        }
        if(isInProgress) {
            throw BoxException.boxIsUsed();
        }
        if(isPending) {
            throw BoxException.boxIsPending();
        }
        boxManager.deleteBox(boxToDelete);
    }

    @Override
    @RolesAllowed("getAllBoxes")
    public List<BoxDto> getAvailableBoxesBetween(Long hotelId, Date dateFrom, Date dateTo) {
        List<Box> boxes = boxManager.getAvailableBoxesBetween(hotelId, dateFrom, dateTo);
        List<BoxDto> result = new ArrayList<>();
        for(Box box : boxes) {
            result.add(Mappers.getMapper(IBoxMapper.class).toBoxDto(box));
        }
        return result;
    }
}
