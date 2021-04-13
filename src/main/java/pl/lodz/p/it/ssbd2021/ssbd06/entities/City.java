package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(catalog = "ssbd06", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c"),
    @NamedQuery(name = "City.findById", query = "SELECT c FROM City c WHERE c.id = :id"),
    @NamedQuery(name = "City.findByName", query = "SELECT c FROM City c WHERE c.name = :name"),
    @NamedQuery(name = "City.findByDescription", query = "SELECT c FROM City c WHERE c.description = :description"),
    @NamedQuery(name = "City.findByVersion", query = "SELECT c FROM City c WHERE c.version = :version"),
    @NamedQuery(name = "City.findByCreationDate", query = "SELECT c FROM City c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "City.findByModificationDate", query = "SELECT c FROM City c WHERE c.modificationDate = :modificationDate")})
public class City implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 31)
    private String name;
    @Size(max = 255)
    private String description;
    @Basic(optional = false)
    @NotNull
    private int version;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cityId")
    private List<Hotel> hotelList;

    public City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(Long id, String name, int version, Date creationDate) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @XmlTransient
    public List<Hotel> getHotelList() {
        return hotelList;
    }

    public void setHotelList(List<Hotel> hotelList) {
        this.hotelList = hotelList;
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
        if (!(object instanceof City)) {
            return false;
        }
        City other = (City) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.City[ id=" + id + " ]";
    }
    
}
