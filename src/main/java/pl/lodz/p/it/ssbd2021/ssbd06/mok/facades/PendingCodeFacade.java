package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PendingCodeFacade extends AbstractFacade<PendingCode> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PendingCodeFacade() {
        super(PendingCode.class);
    }

    public PendingCode findByCode(String code) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findByCode", PendingCode.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    public PendingCode findNotUsedByAccount(Account account) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findNotUsedByAccount", PendingCode.class);
            query.setParameter("account", account);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
