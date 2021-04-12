package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(catalog = "ssbd06", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"),
    @NamedQuery(name = "Rating.findByRate", query = "SELECT r FROM Rating r WHERE r.rate = :rate"),
    @NamedQuery(name = "Rating.findByComment", query = "SELECT r FROM Rating r WHERE r.comment = :comment"),
    @NamedQuery(name = "Rating.findByHidden", query = "SELECT r FROM Rating r WHERE r.hidden = :hidden"),
    @NamedQuery(name = "Rating.findByVersion", query = "SELECT r FROM Rating r WHERE r.version = :version"),
    @NamedQuery(name = "Rating.findByCreationDate", query = "SELECT r FROM Rating r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "Rating.findByModificationDate", query = "SELECT r FROM Rating r WHERE r.modificationDate = :modificationDate")})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Long id;
    @Basic(optional = false)
    @NotNull
    private short rate;
    @Size(max = 255)
    private String comment;
    @Basic(optional = false)
    @NotNull
    private boolean hidden;
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

    public Rating() {
    }

    public Rating(Long id) {
        this.id = id;
    }

    public Rating(Long id, short rate, boolean hidden, int version, Date creationDate) {
        this.id = id;
        this.rate = rate;
        this.hidden = hidden;
        this.version = version;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getRate() {
        return rate;
    }

    public void setRate(short rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating[ id=" + id + " ]";
    }
    
}
