package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Role.ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(name = ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT, columnNames = {"access_level", "account_id"})
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
public abstract class Role extends AbstractEntity implements Serializable {

    public static final String ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT = "uk_role_access_level_account_id";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Column(name = "access_level", updatable = false, nullable = false, insertable = false)
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
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    private Account account;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}
