package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(catalog = "ssbd06", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingLine.findAll", query = "SELECT b FROM BookingLine b"),
    @NamedQuery(name = "BookingLine.findById", query = "SELECT b FROM BookingLine b WHERE b.id = :id"),
    @NamedQuery(name = "BookingLine.findByPricePerDay", query = "SELECT b FROM BookingLine b WHERE b.pricePerDay = :pricePerDay"),
    @NamedQuery(name = "BookingLine.findByVersion", query = "SELECT b FROM BookingLine b WHERE b.version = :version"),
    @NamedQuery(name = "BookingLine.findByCreationDate", query = "SELECT b FROM BookingLine b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "BookingLine.findByModificationDate", query = "SELECT b FROM BookingLine b WHERE b.modificationDate = :modificationDate")})
public class BookingLine implements Serializable {

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

    @JoinColumn(name = "BookingId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Booking bookingId;

    @JoinColumn(name = "BoxId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Box boxId;

    public BookingLine() {
    }

    public BookingLine(Long id) {
        this.id = id;
    }

    public BookingLine(Long id, BigInteger pricePerDay, int version, Date creationDate) {
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

    public Booking getBookingId() {
        return bookingId;
    }

    public void setBookingId(Booking bookingId) {
        this.bookingId = bookingId;
    }

    public Box getBoxId() {
        return boxId;
    }

    public void setBoxId(Box boxId) {
        this.boxId = boxId;
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
        if (!(object instanceof BookingLine)) {
            return false;
        }
        BookingLine other = (BookingLine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine[ id=" + id + " ]";
    }
    
}
