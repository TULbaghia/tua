package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating.BOOKING_ID_CONSTRAINT;


@Entity
@Table(name = "rating", uniqueConstraints = {@UniqueConstraint(name = BOOKING_ID_CONSTRAINT, columnNames = {"booking_id"})},
        indexes = {
        @Index(name = "ix_rating_booking_id", columnList = "booking_id"),
        @Index(name = "ix_rating_created_by", columnList = "created_by"),
        @Index(name = "ix_rating_modified_by", columnList = "modified_by"),
})
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
        @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"),
        @NamedQuery(name = "Rating.findByRate", query = "SELECT r FROM Rating r WHERE r.rate = :rate"),
        @NamedQuery(name = "Rating.findByHidden", query = "SELECT r FROM Rating r WHERE r.hidden = :hidden"),
        @NamedQuery(name = "Rating.getAllRatingsForHotelId", query = "SELECT DISTINCT r FROM Rating r, BookingLine bl WHERE r.booking.id = bl.booking.id AND bl.box.hotel.id = :id")})
@NoArgsConstructor
public class Rating extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String BOOKING_ID_CONSTRAINT = "uk_rating_booking_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Min(1)
    @Max(5)
    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "rate")
    private short rate;

    @Getter
    @Setter
    @Size(min = 4, max = 255)
    @Column(name = "comment")
    private String comment;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "hidden")
    private boolean hidden = false;

    @Getter
    @Setter
    @OneToOne(optional = false)
    private Booking booking;

    public Rating(short rate, boolean hidden) {
        this.rate = rate;
        this.hidden = hidden;
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
}
