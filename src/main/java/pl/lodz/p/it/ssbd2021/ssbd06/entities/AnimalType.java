package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.BaseAbstractEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "animal_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnimalType.findAll", query = "SELECT a FROM AnimalType a"),
    @NamedQuery(name = "AnimalType.findById", query = "SELECT a FROM AnimalType a WHERE a.id = :id"),
    @NamedQuery(name = "AnimalType.findByName", query = "SELECT a FROM AnimalType a WHERE a.name = :name")})
@NoArgsConstructor
@ToString(callSuper = true)
public class AnimalType extends BaseAbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 63)
    private String name;

    @Setter
    @OneToMany(mappedBy = "animalType")
    private List<Box> boxList;


    public AnimalType(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<Box> getBoxList() {
        return boxList;
    }
}
