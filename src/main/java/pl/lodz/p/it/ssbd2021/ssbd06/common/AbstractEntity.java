package pl.lodz.p.it.ssbd2021.ssbd06.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
@ToString(callSuper = true)
public abstract class AbstractEntity extends BaseAbstractEntity {

    @NotNull
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    @NotNull
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date")
    private Date modificationDate;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "modified_by")
    private Account modifiedBy;

    @Getter
    @Version
    private Long version;

    @PrePersist
    private void prePersist() {
        creationDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    private void preUpdate() {
        modificationDate = new Date(System.currentTimeMillis());
    }

}
