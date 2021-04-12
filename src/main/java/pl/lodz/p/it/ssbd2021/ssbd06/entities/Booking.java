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
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findByDateFrom", query = "SELECT b FROM Booking b WHERE b.dateFrom = :dateFrom"),
    @NamedQuery(name = "Booking.findByDateTo", query = "SELECT b FROM Booking b WHERE b.dateTo = :dateTo"),
    @NamedQuery(name = "Booking.findByPrice", query = "SELECT b FROM Booking b WHERE b.price = :price"),
    @NamedQuery(name = "Booking.findByVersion", query = "SELECT b FROM Booking b WHERE b.version = :version"),
    @NamedQuery(name = "Booking.findByCreationDate", query = "SELECT b FROM Booking b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "Booking.findByModificationDate", query = "SELECT b FROM Booking b WHERE b.modificationDate = :modificationDate")})
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Basic(optional = false)
    @NotNull
    private BigInteger price;
    @Basic(optional = false)
    @NotNull
    private int version;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookingId")
    private List<BookingLine> bookingLineList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookingId")
    private List<Rating> ratingList;
    @JoinColumn(name = "AccountId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Account accountId;
    @JoinColumn(name = "StatusId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private BookingStatus statusId;

    public Booking() {
    }

    public Booking(Long id) {
        this.id = id;
    }

    public Booking(Long id, Date dateFrom, Date dateTo, BigInteger price, int version, Date creationDate) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.version = version;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
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

    @XmlTransient
    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public BookingStatus getStatusId() {
        return statusId;
    }

    public void setStatusId(BookingStatus statusId) {
        this.statusId = statusId;
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
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking[ id=" + id + " ]";
    }
    
}
