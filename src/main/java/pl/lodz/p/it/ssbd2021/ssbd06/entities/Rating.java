package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.AbstractEntity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "rating")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
        @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"),
        @NamedQuery(name = "Rating.findByRate", query = "SELECT r FROM Rating r WHERE r.rate = :rate"),
        @NamedQuery(name = "Rating.findByHidden", query = "SELECT r FROM Rating r WHERE r.hidden = :hidden"),
        })
@NoArgsConstructor
@ToString(callSuper = true)
public class Rating extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
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
    private short rate;

    @Getter
    @Setter
    @Size(min = 4, max = 255)
    private String comment;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
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
}
