package pl.lodz.p.it.ssbd2021.ssbd06.common;

import lombok.ToString;

import javax.persistence.*;

@MappedSuperclass
@ToString
public abstract class BaseAbstractEntity {

    public abstract Long getId();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != this.getClass()) {
            return false;
        }
        BaseAbstractEntity other = (BaseAbstractEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

}
