package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "hotel", uniqueConstraints = {
        @UniqueConstraint(name = "uk_hotel_name", columnNames = "name")
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
@ToString(callSuper = true)
public class Hotel extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Box> boxList;

    @Setter
    @OneToMany(mappedBy = "hotel")
    private List<ManagerData> managerDataList;

    @Getter
    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private City city;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<Box> getBoxList() {
        return boxList;
    }

    @XmlTransient
    public List<ManagerData> getManagerDataList() {
        return managerDataList;
    }
}
