package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.BaseAbstractEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "booking_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingStatus.findAll", query = "SELECT b FROM BookingStatus b"),
    @NamedQuery(name = "BookingStatus.findById", query = "SELECT b FROM BookingStatus b WHERE b.id = :id"),
    @NamedQuery(name = "BookingStatus.findByName", query = "SELECT b FROM BookingStatus b WHERE b.name = :name")})
@NoArgsConstructor
@ToString(callSuper = true)
public class BookingStatus extends BaseAbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 63)
    private String name;

    @Setter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<Booking> bookingList;

    public BookingStatus(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<Booking> getBookingList() {
        return bookingList;
    }
}
