package pl.lodz.p.it.ssbd2021.ssbd06.auth.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;

@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd06authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
}
