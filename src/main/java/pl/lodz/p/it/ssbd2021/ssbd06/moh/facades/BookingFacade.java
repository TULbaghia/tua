package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

import org.apache.commons.lang3.NotImplementedException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class BookingFacade extends AbstractFacade<Booking>{

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingFacade() {
        super(Booking.class);
    }

    /**
     * Zwraca listę aktywnych rezerwacji
     * @return lista aktywnych rezerwacji
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji pobrania aktywnych rezerwaacji
     */
    @RolesAllowed({"getAllActiveReservations", "deleteHotel"})
    public List<Booking> findAllActive() throws AppBaseException {
        try {
            TypedQuery<Booking> bookingTypedQuery = em.createNamedQuery("Booking.findAllActive", Booking.class);
            return bookingTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    /**
     * Zwraca listę archiwalnych rezerwacji
     * @return lista archiwalnych rezerwacji
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji pobrania archiwalnych rezerwaacji
     */
    @RolesAllowed({"getAllArchiveReservations", "getEndedBookingsForHotel"})
    public List<Booking> findAllArchived() throws AppBaseException {
        try {
            TypedQuery<Booking> bookingTypedQuery = em.createNamedQuery("Booking.findAllArchived", Booking.class);
            return bookingTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @RolesAllowed("bookReservation")
    @Override
    public void create(Booking entity) throws AppBaseException {
        super.create(entity);
    }

    @RolesAllowed({"startReservation", "cancelReservation", "endReservation"})
    @Override
    public void edit(Booking entity) throws AppBaseException {
        super.edit(entity);
    }

    @DenyAll
    @Override
    public void remove(Booking entity) throws AppBaseException {
        super.remove(entity);
    }

    @RolesAllowed({"addHotelRating", "startReservation", "cancelReservation", "endReservation", "getReservation"})
    @Override
    public Booking find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @DenyAll
    @Override
    public List<Booking> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<Booking> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
