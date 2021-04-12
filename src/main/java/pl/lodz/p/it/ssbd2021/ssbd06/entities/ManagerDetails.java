package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "manager_details")
@DiscriminatorValue("Manager")
@NamedQueries({
    @NamedQuery(name = "ManagerDetails.findAll", query = "SELECT m FROM ManagerDetails m"),
    @NamedQuery(name = "ManagerDetails.findById", query = "SELECT m FROM ManagerDetails m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class ManagerDetails extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

//    @JoinColumn(name = "HotelId", referencedColumnName = "Id")
//    @ManyToOne(optional = false)
//    private Hotel hotelId;

//    public Hotel getHotelId() {
//        return hotelId;
//    }
//
//    public void setHotelId(Hotel hotelId) {
//        this.hotelId = hotelId;
//    }
}
