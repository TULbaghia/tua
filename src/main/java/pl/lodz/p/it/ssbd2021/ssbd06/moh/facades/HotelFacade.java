package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class HotelFacade extends AbstractFacade<Hotel> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HotelFacade() {
        super(Hotel.class);
    }

    @PermitAll
    public Hotel findByName(String name){
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<Hotel> findByFilter(String... filter){
        throw new UnsupportedOperationException();
    }

    @PermitAll
    @Override
    public void create(Hotel entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void edit(Hotel entity) throws AppBaseException {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(Hotel entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch(ConstraintViolationException e) {
            if (entity.getManagerDataList() != null) {
                throw HotelException.deleteHasManager();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @PermitAll
    @Override
    public Hotel find(Object id) { return super.find(id); }

    @PermitAll
    @Override
    public List<Hotel> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<Hotel> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
