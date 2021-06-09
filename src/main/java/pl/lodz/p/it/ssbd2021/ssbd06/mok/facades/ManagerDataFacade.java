package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ManagerDataFacade extends AbstractFacade<ManagerData> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @PermitAll
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManagerDataFacade() {
        super(ManagerData.class);
    }

    @DenyAll
    @Override
    public void create(ManagerData entity) throws AppBaseException {
        super.create(entity);
    }

    @DenyAll
    @Override
    public void edit(ManagerData entity) throws AppBaseException {
        super.edit(entity);
    }

    @DenyAll
    @Override
    public void remove(ManagerData entity) throws AppBaseException {
        super.remove(entity);
    }

    @DenyAll
    @Override
    public ManagerData find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @DenyAll
    @Override
    public List<ManagerData> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<ManagerData> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
