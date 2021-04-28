package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "booking", indexes = {
        @Index(name = "ix_booking_account_id", columnList = "account_id"),
        @Index(name = "ix_booking_created_by", columnList = "created_by"),
        @Index(name = "ix_booking_modified_by", columnList = "modified_by")
})
@Check(constraints = "date_from < date_to")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
        @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class Booking extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking_id")
    @SequenceGenerator(name = "seq_booking_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_from")
    private Date dateFrom;

    @NotNull
    @Getter
    @Setter
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_to")
    private Date dateTo;

    @Getter
    @Setter
    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "booking")
    private Set<BookingLine> bookingLineList = new HashSet<>();

    @Getter
    @Setter
    @JoinColumn(name = "rating_id", referencedColumnName = "id")
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "booking")
    private Rating rating;

    @Getter
    @Setter
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account account;

    @Getter
    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
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
    public Set<BookingLine> getBookingLineList() {
        return bookingLineList;
    }
}