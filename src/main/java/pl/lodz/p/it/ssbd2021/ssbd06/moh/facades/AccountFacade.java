package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import org.apache.commons.lang3.NotImplementedException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
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
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @PermitAll
    public Account findByLogin(String login){
        throw new UnsupportedOperationException();
    }

    @PermitAll
    @Override
    public void create(Account entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void edit(Account entity) throws AppBaseException {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(Account entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public Account find(Object id) {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<Account> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<Account> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
