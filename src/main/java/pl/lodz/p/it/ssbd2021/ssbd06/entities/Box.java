package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.BoxDescription;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
        @NamedQuery(name = "Box.findByPricePerDay", query = "SELECT b FROM Box b WHERE b.pricePerDay = :pricePerDay"),
        @NamedQuery(name = "getAvailableBoxesByTypesAndHotelId", query = "SELECT b FROM Box b WHERE " +
                "b.hotel.id = :hotel_id " +
                "AND b.animalType IN :types " +
                "AND b.id NOT IN (SELECT bl.box.id FROM BookingLine bl WHERE bl.booking.dateTo >= :dateFrom AND bl.booking.dateFrom <= :dateTo) " +
                "AND b.delete = false"
        ),
        @NamedQuery(name = "getAvailableBoxesByTypesByHotelIdAndBetween", query = "SELECT b FROM Box b WHERE " +
                "b.hotel.id = :hotel_id " +
                "AND b.id NOT IN (SELECT bl.box.id FROM BookingLine bl WHERE bl.booking.dateTo >= :dateFrom AND bl.booking.dateFrom <= :dateTo) " +
                "AND b.delete = false"
        ),
        @NamedQuery(name = "getAvailableBoxesByIdListAndHotelId", query = "SELECT b FROM Box b WHERE " +
                "b.hotel.id = :hotel_id " +
                "AND b.id NOT IN (SELECT bl.box.id FROM BookingLine bl WHERE bl.booking.dateTo >= :dateFrom AND bl.booking.dateFrom <= :dateTo AND (bl.booking.status = pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus.IN_PROGRESS OR bl.booking.status = pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.BookingStatus.PENDING))  " +
                "AND b.id IN :boxIdList " +
                "AND b.delete = false"
        ),
})
@NoArgsConstructor
public class Box extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Getter
    @Setter
    @NotNull
    @NonNull
    @Size(min = 1, max = 31)
    @Column(name = "description", length = 31, nullable = false)
    @BoxDescription
    private String description;

    @Setter
    @OneToMany(mappedBy = "box")
    private Set<BookingLine> bookingLineList = new HashSet<>();

    @Getter
    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "animal_type", nullable = false)
    private AnimalType animalType;

    @Getter
    @Setter
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private Hotel hotel;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "delete")
    private boolean delete = false;

    public Box(BigDecimal pricePerDay, Hotel hotel, AnimalType animalType, String description, boolean delete) {
        this.pricePerDay = pricePerDay;
        this.hotel = hotel;
        this.animalType = animalType;
        this.description = description;
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }

    @XmlTransient
    public Set<BookingLine> getBookingLineList() {
        return bookingLineList;
    }
}
