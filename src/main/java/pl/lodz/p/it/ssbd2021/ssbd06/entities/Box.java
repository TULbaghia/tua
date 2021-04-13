package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(catalog = "ssbd06", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Box.findAll", query = "SELECT b FROM Box b"),
    @NamedQuery(name = "Box.findById", query = "SELECT b FROM Box b WHERE b.id = :id"),
    @NamedQuery(name = "Box.findByPricePerDay", query = "SELECT b FROM Box b WHERE b.pricePerDay = :pricePerDay"),
    @NamedQuery(name = "Box.findByVersion", query = "SELECT b FROM Box b WHERE b.version = :version"),
    @NamedQuery(name = "Box.findByCreationDate", query = "SELECT b FROM Box b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "Box.findByModificationDate", query = "SELECT b FROM Box b WHERE b.modificationDate = :modificationDate")})
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long id;
    @Basic(optional = false)
    @NotNull
    private BigInteger pricePerDay;
    @Basic(optional = false)
    @NotNull
    private int version;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "boxId")
    private List<BookingLine> bookingLineList;
    @JoinColumn(name = "AnimalTypeId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private AnimalType animalTypeId;
    @JoinColumn(name = "HotelId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Hotel hotelId;

    public Box() {
    }

    public Box(Long id) {
        this.id = id;
    }

    public Box(Long id, BigInteger pricePerDay, int version, Date creationDate) {
        this.id = id;
        this.pricePerDay = pricePerDay;
        this.version = version;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigInteger pricePerDay) {
        this.pricePerDay = pricePerDay;
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
    public List<BookingLine> getBookingLineList() {
        return bookingLineList;
    }

    public void setBookingLineList(List<BookingLine> bookingLineList) {
        this.bookingLineList = bookingLineList;
    }

    public AnimalType getAnimalTypeId() {
        return animalTypeId;
    }

    public void setAnimalTypeId(AnimalType animalTypeId) {
        this.animalTypeId = animalTypeId;
    }

    public Hotel getHotelId() {
        return hotelId;
    }

    public void setHotelId(Hotel hotelId) {
        this.hotelId = hotelId;
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
        if (!(object instanceof Box)) {
            return false;
        }
        Box other = (Box) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.Box[ id=" + id + " ]";
    }
    
}
