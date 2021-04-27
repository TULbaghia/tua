package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.TargetType;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReferenceMapper {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager entityManager;

    public <T extends AbstractEntity> T resolve(Long id, @TargetType Class<T> entityClass){
        return id != null ? entityManager.find( entityClass, id) : null;
    }

    public Long toReference(AbstractEntity entity) {
        return entity != null ? entity.getId() : null;
    }
}
