package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class RatingFacade extends AbstractFacade<Rating> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RatingFacade() {
        super(Rating.class);
    }

    @RolesAllowed("addHotelRating")
    @Override
    public void create(Rating entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Rating.BOOKING_ID_CONSTRAINT)) {
                throw RatingException.bookingAlreadyRated(e);
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @RolesAllowed({"updateHotelRating", "hideHotelRating"})
    @Override
    public void edit(Rating entity) throws AppBaseException {
        super.edit(entity);
    }

    @RolesAllowed("deleteHotelRating")
    @Override
    public void remove(Rating entity) throws AppBaseException {
        super.remove(entity);
    }

    @RolesAllowed({"deleteHotelRating", "getHotelRating", "hideHotelRating"})
    @Override
    public Rating find(Object id) throws AppBaseException {
        return super.find(id);
    }

    /**
     * Zwraca listę wszystkich ocen w systemie.
     *
     * @return lista ocen
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @PermitAll
    @Override
    public List<Rating> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<Rating> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }

    /**
     * Zwraca wszystkie oceny dla hotelu o danym id.
     *
     * @param hotelId id hotelu
     * @return Lista ocen
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji pobrania ocen
     */
    @RolesAllowed({"deleteHotelRating", "addHotelRating", "updateHotelRating"})
    public List<Rating> getAllRatingsForHotelId(Long hotelId) throws AppBaseException {
        try {
            TypedQuery<Rating> ratingTypedQuery = em.createNamedQuery("Rating.getAllRatingsForHotelId", Rating.class);
            ratingTypedQuery.setParameter("id", hotelId);
            return ratingTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.ratingNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
