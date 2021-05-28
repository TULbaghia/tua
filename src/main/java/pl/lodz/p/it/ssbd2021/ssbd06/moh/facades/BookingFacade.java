package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.NotImplementedException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
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

    @PermitAll
    public List<Booking> findAllActive(){
        throw new NotImplementedException();
    }

    @PermitAll
    public List<Booking> findAllArchived(){
        throw new NotImplementedException();
    }

    @PermitAll
    @Override
    public void create(Booking entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void edit(Booking entity) throws AppBaseException {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(Booking entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public Booking find(Object id) {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<Booking> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<Booking> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
