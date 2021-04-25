package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

@Entity
@Table(name = "booking_line", indexes = {
        @Index(name = "ix_booking_line_booking_id", columnList = "booking_id"),
        @Index(name = "ix_booking_line_box_id", columnList = "box_id"),
        @Index(name = "ix_booking_line_created_by", columnList = "created_by"),
        @Index(name = "ix_booking_line_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingLine.findAll", query = "SELECT b FROM BookingLine b"),
    @NamedQuery(name = "BookingLine.findById", query = "SELECT b FROM BookingLine b WHERE b.id = :id"),
    @NamedQuery(name = "BookingLine.findByPricePerDay", query = "SELECT b FROM BookingLine b WHERE b.pricePerDay = :pricePerDay")})
@NoArgsConstructor
@ToString(callSuper = true)
public class BookingLine extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking_line_id")
    @SequenceGenerator(name = "seq_booking_line_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Setter
    @Getter
    @NotNull
    @Min(value = 0)
    @Digits(integer = 6, fraction = 2)
    @Basic(optional = false)
    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Getter
    @Setter
    @JoinColumn(name = "booking_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Booking booking;

    @Getter
    @Setter
    @JoinColumn(name = "box_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true)
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
