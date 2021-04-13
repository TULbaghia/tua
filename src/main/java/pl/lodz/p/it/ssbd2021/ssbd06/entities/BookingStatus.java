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
    @NamedQuery(name = "BookingStatus.findAll", query = "SELECT b FROM BookingStatus b"),
    @NamedQuery(name = "BookingStatus.findById", query = "SELECT b FROM BookingStatus b WHERE b.id = :id"),
    @NamedQuery(name = "BookingStatus.findByName", query = "SELECT b FROM BookingStatus b WHERE b.name = :name")})
public class BookingStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 63)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusId")
    private List<Booking> bookingList;

    public BookingStatus() {
    }

    public BookingStatus(Integer id) {
        this.id = id;
    }

    public BookingStatus(Integer id, String name) {
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
    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
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
        if (!(object instanceof BookingStatus)) {
            return false;
        }
        BookingStatus other = (BookingStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingStatus[ id=" + id + " ]";
    }
    
}
