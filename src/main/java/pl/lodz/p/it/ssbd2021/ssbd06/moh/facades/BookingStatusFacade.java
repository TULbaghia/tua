package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;

@Stateless
public class BookingStatusFacade extends AbstractFacade<BookingStatus> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingStatusFacade() {
        super(BookingStatus.class);
    }
    
}
