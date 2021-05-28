package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class BookingLineFacade extends AbstractFacade<BookingLine> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingLineFacade() {
        super(BookingLine.class);
    }

    @PermitAll
    @Override
    public void create(BookingLine entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void edit(BookingLine entity) throws AppBaseException {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(BookingLine entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public BookingLine find(Object id) {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<BookingLine> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<BookingLine> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
