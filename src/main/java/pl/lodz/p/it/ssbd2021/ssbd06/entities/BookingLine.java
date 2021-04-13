package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.AbstractEntity;

@Entity
@Table(name = "booking_line")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingLine.findAll", query = "SELECT b FROM BookingLine b"),
    @NamedQuery(name = "BookingLine.findById", query = "SELECT b FROM BookingLine b WHERE b.id = :id"),
    @NamedQuery(name = "BookingLine.findByPricePerDay", query = "SELECT b FROM BookingLine b WHERE b.pricePerDay = :pricePerDay")})
@NoArgsConstructor
@ToString(callSuper = true)
public class BookingLine extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Setter
    @Getter
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2)
    @Basic(optional = false)
    private BigDecimal pricePerDay;

    @Getter
    @Setter
    @JoinColumn(name = "booking", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Booking booking;

    @Getter
    @Setter
    @JoinColumn(name = "box", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Box box;

    public BookingLine(BigDecimal pricePerDay, Booking booking, Box box) {
        this.pricePerDay = pricePerDay;
        this.booking = booking;
        this.box = box;
    }

    public Long getId() {
        return id;
    }
}
