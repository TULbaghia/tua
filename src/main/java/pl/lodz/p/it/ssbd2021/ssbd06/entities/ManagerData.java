package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "manager_data", indexes = {
        @Index(name = "ix_manager_data_hotel_id", columnList = "hotel_id")
})
@DiscriminatorValue("MANAGER")
@NamedQueries({
        @NamedQuery(name = "ManagerData.findAll", query = "SELECT m FROM ManagerData m"),
        @NamedQuery(name = "ManagerData.findById", query = "SELECT m FROM ManagerData m WHERE m.id = :id")})
@NoArgsConstructor
public class ManagerData extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Hotel hotel;

    @Override public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this)
                .append(super.toString());
        if (hotel != null) {
            toStringBuilder.append("hotel", hotel.getId());
        }
        return toStringBuilder.toString();
    }
}
