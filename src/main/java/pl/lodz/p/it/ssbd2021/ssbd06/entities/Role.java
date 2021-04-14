package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_access_level_account_id", columnNames = {"access_level", "account_id"})
}, indexes = {
        @Index(name = "ix_role_account_id", columnList = "account_id"),
        @Index(name = "ix_role_created_by", columnList = "created_by"),
        @Index(name = "ix_role_modified_by", columnList = "modified_by")
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "Role.findByEnabled", query = "SELECT r FROM Role r WHERE r.enabled = :enabled")})
@NoArgsConstructor
@ToString(callSuper = true)
public class Role extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role_id")
    @SequenceGenerator(name = "seq_role_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "access_level", updatable = false)
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @Getter
    @Setter
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled = true;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable= false)
    private Account account;

    @Override
    public Long getId() {
        return id;
    }
}
