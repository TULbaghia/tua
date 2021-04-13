package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import pl.lodz.p.it.ssbd2021.ssbd06.common.BaseAbstractEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "code_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodeType.findAll", query = "SELECT c FROM CodeType c"),
    @NamedQuery(name = "CodeType.findById", query = "SELECT c FROM CodeType c WHERE c.id = :id"),
    @NamedQuery(name = "CodeType.findByName", query = "SELECT c FROM CodeType c WHERE c.name = :name")})
@NoArgsConstructor
@ToString(callSuper = true)
public class CodeType extends BaseAbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Getter
    @Setter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 63)
    @Column(length = 64)
    private String name;

    @Setter
    @OneToMany(mappedBy = "codeType")
    private List<PendingCode> pendingCodeList;

    public CodeType(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @XmlTransient
    public List<PendingCode> getPendingCodeList() {
        return pendingCodeList;
    }
}
