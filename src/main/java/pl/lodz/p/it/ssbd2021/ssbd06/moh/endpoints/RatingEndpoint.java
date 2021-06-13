package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewRatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.RatingEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.RatingManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie ocenami hoteli.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RatingEndpoint  extends AbstractEndpoint implements RatingEndpointLocal {

    @Inject
    private RatingManager ratingManager;

    @Override
    public List<RatingDto> getAll(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("addHotelRating")
    public void addRating(NewRatingDto ratingDto) throws AppBaseException {
        ratingManager.addRating(ratingDto);
    }

    @Override
    @RolesAllowed("updateHotelRating")
    public void updateRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("deleteHotelRating")
    public void deleteRating(Long ratingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("hideHotelRating")
    public void changeVisibility(Long ratingId, RatingVisibility ratingVisibility) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
