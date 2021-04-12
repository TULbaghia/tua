package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(catalog = "ssbd06", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnimalType.findAll", query = "SELECT a FROM AnimalType a"),
    @NamedQuery(name = "AnimalType.findById", query = "SELECT a FROM AnimalType a WHERE a.id = :id"),
    @NamedQuery(name = "AnimalType.findByName", query = "SELECT a FROM AnimalType a WHERE a.name = :name")})
public class AnimalType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 63)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animalTypeId")
    private List<Box> boxList;

    public AnimalType() {
    }

    public AnimalType(Integer id) {
        this.id = id;
    }

    public AnimalType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<Box> boxList) {
        this.boxList = boxList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnimalType)) {
            return false;
        }
        AnimalType other = (AnimalType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.AnimalType[ id=" + id + " ]";
    }
    
}
