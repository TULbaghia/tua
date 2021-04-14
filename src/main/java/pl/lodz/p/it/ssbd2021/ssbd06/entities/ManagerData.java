package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "manager_data", indexes = {
        @Index(name = "ix_manager_details_hotel_id", columnList = "hotel_id")
})
@DiscriminatorValue("MANAGER")
@NamedQueries({
    @NamedQuery(name = "ManagerDetails.findAll", query = "SELECT m FROM ManagerData m"),
    @NamedQuery(name = "ManagerDetails.findById", query = "SELECT m FROM ManagerData m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class ManagerData extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne()
    private Hotel hotel;
}
