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
@Table(name = "box")
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @DecimalMin(value = "0")
    @Digits(integer = 8, fraction = 2)
    @Basic(optional = false)
    private BigDecimal pricePerDay;

    @Setter
    @OneToMany(mappedBy = "box")
    private List<BookingLine> bookingLineList;

    @Getter
    @Setter
    @JoinColumn(name = "animalType", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AnimalType animalType;

    @Getter
    @Setter
    @JoinColumn(name = "hotel", referencedColumnName = "id")
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
