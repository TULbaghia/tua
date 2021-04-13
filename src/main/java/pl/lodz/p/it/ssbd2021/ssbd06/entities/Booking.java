package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class Booking extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @NotNull
    @Getter
    @Setter
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Getter
    @Setter
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2)
    @Basic(optional = false)
    @NotNull
    private BigDecimal price;

    @Setter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookingLine> bookingLineList;

    @Getter
    @Setter
    @JoinColumn(name = "rating", referencedColumnName = "id")
    @OneToOne(mappedBy = "booking")
    private Rating rating;

    @Getter
    @Setter
    @JoinColumn(name = "account", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Account account;

    @Getter
    @Setter
    @JoinColumn(name = "status", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BookingStatus status;

    public Booking(Date dateFrom, Date dateTo, BigDecimal price, Account account) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<BookingLine> getBookingLineList() {
        return bookingLineList;
    }
}
