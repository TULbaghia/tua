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

@Entity
@Table(name = "rating", indexes = {
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
        })
@NoArgsConstructor
public class Rating extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rating_id")
    @SequenceGenerator(name = "seq_rating_id", allocationSize = 1)
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
