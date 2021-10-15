package pl.lodz.p.it.ssbd2021.ssbd06.auth.facades;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountFacadeAuth extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd06authPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacadeAuth() {
        super(Account.class);
    }
    
}
