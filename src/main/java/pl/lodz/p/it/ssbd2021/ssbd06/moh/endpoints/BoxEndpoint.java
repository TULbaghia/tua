package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IBoxMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BoxEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.managers.BoxManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
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

    @Override
    public BoxDto get(Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
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
    @RolesAllowed("addBox")
    public void addBox(NewBoxDto boxDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("updateBox")
    public void updateBox(BoxDto boxDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("deleteBox")
    public void deleteBox(Long boxId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
