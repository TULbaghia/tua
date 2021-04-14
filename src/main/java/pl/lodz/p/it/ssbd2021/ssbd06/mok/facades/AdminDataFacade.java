package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.AdminData;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;

@Stateless
public class AdminDataFacade extends AbstractFacade<AdminData> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminDataFacade() {
        super(AdminData.class);
    }
    
}
