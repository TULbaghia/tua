package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RatingException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRatingMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.RatingEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.RatingManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie ocenami hoteli.
 */
@DeclareRoles("Admin")
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RatingEndpoint extends AbstractEndpoint implements RatingEndpointLocal {

    @Inject
    private RatingManager ratingManager;

    @Resource(name = "sessionContext")
    SessionContext sessionContext;

    @RolesAllowed({"getHotelRating"})
    public RatingDto get(Long id) throws AppBaseException {
        return Mappers.getMapper(IRatingMapper.class).toRatingDto(ratingManager.get(id));
    }

    @Override
    @PermitAll
    public List<RatingDto> getAll(Long hotelId) throws AppBaseException {
        List<Rating> ratings = ratingManager.getAll(hotelId);
        List<RatingDto> result = new ArrayList<>();
        for (Rating rating : ratings) {
            result.add(Mappers.getMapper(IRatingMapper.class).toRatingDto(rating));
        }
        return result;
    }

    @Override
    @RolesAllowed({"getHotelRating"})
    public RatingDto getRating(Long ratingId) throws AppBaseException {
        Rating rating = ratingManager.getRating(ratingId);
        if (getLogin().equals(rating.getBooking().getAccount().getLogin()) || sessionContext.isCallerInRole("Admin")) {
            return Mappers.getMapper(IRatingMapper.class).toRatingDto(rating);
        } else {
            throw RatingException.accessDenied();
        }
    }

    @Override
    @RolesAllowed("addHotelRating")
    public void addRating(NewRatingDto ratingDto) throws AppBaseException {
        ratingManager.addRating(ratingDto);
    }

    @Override
    @RolesAllowed("updateHotelRating")
    public void updateRating(UpdateRatingDto updateRatingDto) throws AppBaseException {
        Rating rating = ratingManager.getRating(updateRatingDto.getId());
        if (!getLogin().equals(rating.getCreatedBy().getLogin())) {
            throw RatingException.accessDenied();
        }
        RatingDto ratingIntegrity = Mappers.getMapper(IRatingMapper.class).toRatingDto(rating);
        if (!verifyIntegrity(ratingIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        ratingManager.updateRating(updateRatingDto);
    }

    @Override
    @RolesAllowed("deleteHotelRating")
    public void deleteRating(Long ratingId) throws AppBaseException {
        Rating rating = ratingManager.get(ratingId);
        RatingDto ratingIntegrity = Mappers.getMapper(IRatingMapper.class).toRatingDto(rating);
        if (!verifyIntegrity(ratingIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        ratingManager.deleteRating(ratingId);
    }

    @Override
    @RolesAllowed("hideHotelRating")
    public void changeVisibility(Long ratingId) throws AppBaseException {
        Rating rating = ratingManager.getRating(ratingId);

        RatingDto ratingIntegrity = Mappers.getMapper(IRatingMapper.class).toRatingDto(rating);
        if (!verifyIntegrity(ratingIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }

        ratingManager.changeVisibility(ratingId);
    }
}
