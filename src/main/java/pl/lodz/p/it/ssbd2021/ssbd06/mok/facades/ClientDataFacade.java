package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.ClientData;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClientDataFacade extends AbstractFacade<ClientData> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @PermitAll
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientDataFacade() {
        super(ClientData.class);
    }

    @PermitAll
    @Override
    public void create(ClientData entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void edit(ClientData entity) throws AppBaseException {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(ClientData entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public ClientData find(Object id) {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<ClientData> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<ClientData> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
