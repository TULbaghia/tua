package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "Hotel.findAll", query = "SELECT h FROM Hotel h"),
    @NamedQuery(name = "Hotel.findById", query = "SELECT h FROM Hotel h WHERE h.id = :id"),
    @NamedQuery(name = "Hotel.findByName", query = "SELECT h FROM Hotel h WHERE h.name = :name"),
    @NamedQuery(name = "Hotel.findByRating", query = "SELECT h FROM Hotel h WHERE h.rating = :rating"),
    @NamedQuery(name = "Hotel.findByAddress", query = "SELECT h FROM Hotel h WHERE h.address = :address"),
    @NamedQuery(name = "Hotel.findByVersion", query = "SELECT h FROM Hotel h WHERE h.version = :version"),
    @NamedQuery(name = "Hotel.findByCreationDate", query = "SELECT h FROM Hotel h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "Hotel.findByModificationDate", query = "SELECT h FROM Hotel h WHERE h.modificationDate = :modificationDate")})
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 63)
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    private BigDecimal rating;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 63)
    private String address;
    @Basic(optional = false)
    @NotNull
    private int version;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotelId")
    private List<Box> boxList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotelId")
    private List<ManagerDetails> managerDetailsList;
    @JoinColumn(name = "CityId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private City cityId;

    public Hotel() {
    }

    public Hotel(Long id) {
        this.id = id;
    }

    public Hotel(Long id, String name, BigDecimal rating, String address, int version, Date creationDate) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
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

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public List<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<Box> boxList) {
        this.boxList = boxList;
    }

    @XmlTransient
    public List<ManagerDetails> getManagerDetailsList() {
        return managerDetailsList;
    }

    public void setManagerDetailsList(List<ManagerDetails> managerDetailsList) {
        this.managerDetailsList = managerDetailsList;
    }

    public City getCityId() {
        return cityId;
    }

    public void setCityId(City cityId) {
        this.cityId = cityId;
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
        if (!(object instanceof Hotel)) {
            return false;
        }
        Hotel other = (Hotel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel[ id=" + id + " ]";
    }
    
}
