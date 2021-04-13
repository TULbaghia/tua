package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.AbstractEntity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accessLevel", "account"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "accessLevel")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "Role.findByEnabled", query = "SELECT r FROM Role r WHERE r.enabled = :enabled")})
@NoArgsConstructor
@ToString(callSuper = true)
public class Role extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Getter
    @NotNull
    @Size(min = 1, max = 16)
    @Column(updatable = false)
    @Basic(optional = false)
    private String accessLevel;

    @Getter
    @Setter
    @Basic(optional = false)
    private boolean enabled = true;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account", referencedColumnName = "id", updatable= false)
    private Account account;

    @Override
    public Long getId() {
        return id;
    }
}
