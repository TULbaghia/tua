package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "box", indexes = {
        @Index(name = "ix_box_hotel_id", columnList = "hotel_id"),
        @Index(name = "ix_box_created_by", columnList = "created_by"),
        @Index(name = "ix_box_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Box.findAll", query = "SELECT b FROM Box b"),
    @NamedQuery(name = "Box.findById", query = "SELECT b FROM Box b WHERE b.id = :id"),
    @NamedQuery(name = "Box.findByPricePerDay", query = "SELECT b FROM Box b WHERE b.pricePerDay = :pricePerDay")})
@NoArgsConstructor
@ToString(callSuper = true)
public class Box extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_box_id")
    @SequenceGenerator(name = "seq_box_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Min(value = 0)
    @Digits(integer = 6, fraction = 2)
    @Basic(optional = false)
    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Setter
    @OneToMany(mappedBy = "box")
    private List<BookingLine> bookingLineList;

    @Getter
    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "animal_type")
    private AnimalType animalType;

    @Getter
    @Setter
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Hotel hotel;

    public Box(BigDecimal pricePerDay, Hotel hotel, AnimalType animalType) {
        this.pricePerDay = pricePerDay;
        this.hotel = hotel;
        this.animalType = animalType;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<BookingLine> getBookingLineList() {
        return bookingLineList;
    }
}
