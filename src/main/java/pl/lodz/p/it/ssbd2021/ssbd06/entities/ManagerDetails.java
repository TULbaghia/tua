package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "manager_details", indexes = {
        @Index(name = "ix_manager_details_hotel", columnList = "hotel")
})
@DiscriminatorValue("Manager")
@NamedQueries({
    @NamedQuery(name = "ManagerDetails.findAll", query = "SELECT m FROM ManagerDetails m"),
    @NamedQuery(name = "ManagerDetails.findById", query = "SELECT m FROM ManagerDetails m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class ManagerDetails extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JoinColumn(name = "hotel", referencedColumnName = "id")
    @ManyToOne()
    private Hotel hotel;
}
