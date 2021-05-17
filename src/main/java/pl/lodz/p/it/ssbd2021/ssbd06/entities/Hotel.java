package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel.HOTEL_NAME_CONSTRAINT;

@Entity
@Table(name = "hotel", uniqueConstraints = {
        @UniqueConstraint(name = HOTEL_NAME_CONSTRAINT, columnNames = "name")
}, indexes = {
        @Index(name = "ix_hotel_city_id", columnList = "city_id"),
        @Index(name = "ix_hotel_created_by", columnList = "created_by"),
        @Index(name = "ix_hotel_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Hotel.findAll", query = "SELECT h FROM Hotel h"),
        @NamedQuery(name = "Hotel.findById", query = "SELECT h FROM Hotel h WHERE h.id = :id"),
        @NamedQuery(name = "Hotel.findByName", query = "SELECT h FROM Hotel h WHERE h.name = :name")})
@NoArgsConstructor
public class Hotel extends AbstractEntity implements Serializable {

    public static final String HOTEL_NAME_CONSTRAINT = "uk_hotel_name";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hotel_id")
    @SequenceGenerator(name = "seq_hotel_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 63)
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Digits(integer = 1, fraction = 1)
    @Column(name = "rating")
    private BigDecimal rating;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 63)
    @Column(name = "address")
    private String address;

    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            mappedBy = "hotel", orphanRemoval = true)
    private Set<Box> boxList = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<ManagerData> managerDataList = new HashSet<>();

    @Getter
    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private City city;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .append("name", name)
                .append("rating", rating)
                .append("address", address)
                .append("boxList", boxList)
                .append("managerDataList", managerDataList)
                .append("city", city.getName())
                .toString();
    }

    @XmlTransient
    public Set<Box> getBoxList() {
        return boxList;
    }

    @XmlTransient
    public Set<ManagerData> getManagerDataList() {
        return managerDataList;
    }
}
