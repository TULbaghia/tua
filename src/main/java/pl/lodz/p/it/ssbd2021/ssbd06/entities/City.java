package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.ssbd2021.ssbd06.entities.City.CITY_CONSTRAINT;

@Entity
@Table(name = "city", uniqueConstraints = {
        @UniqueConstraint(name = CITY_CONSTRAINT, columnNames = {"name"})
}, indexes = {
        @Index(name = "ix_city_created_by", columnList = "created_by"),
        @Index(name = "ix_city_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c"),
    @NamedQuery(name = "City.findById", query = "SELECT c FROM City c WHERE c.id = :id"),
    @NamedQuery(name = "City.findByName", query = "SELECT c FROM City c WHERE c.name = :name")})
@NoArgsConstructor
public class City extends AbstractEntity implements Serializable {

    public static final String CITY_CONSTRAINT = "uk_city_name";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_city_id")
    @SequenceGenerator(name = "seq_city_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 31)
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Basic(optional = false)
    @Size(min = 4, max = 255)
    @Column(name = "description")
    private String description;

    @Setter
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "city")
    private Set<Hotel> hotelList = new HashSet<>();

    public City(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public Set<Hotel> getHotelList() {
        return hotelList;
    }
	
	@Override public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}
