package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pending_code", uniqueConstraints = {
        @UniqueConstraint(name = "uk_pending_code_code", columnNames = {"code"})
}, indexes = {
        @Index(name = "ix_pending_code_account_id", columnList = "account_id"),
        @Index(name = "ix_pending_code_created_by", columnList = "created_by"),
        @Index(name = "ix_pending_code_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PendingCode.findAll", query = "SELECT p FROM PendingCode p"),
    @NamedQuery(name = "PendingCode.findById", query = "SELECT p FROM PendingCode p WHERE p.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class PendingCode extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pending_code_id")
    @SequenceGenerator(name = "seq_pending_code_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 128)
    @Basic(optional = false)
    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Getter
    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "used", nullable = false)
    private boolean used;

    @Getter
    @Setter
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
    private Account account;

    @Getter
    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "code_type", nullable = false)
    private CodeType codeType;

    public PendingCode(String code, boolean used) {
        this.code = code;
        this.used = used;
    }

    @Override
    public Long getId() {
        return id;
    }
}