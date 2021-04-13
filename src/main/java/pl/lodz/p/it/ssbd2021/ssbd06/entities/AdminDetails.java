package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_details")
@DiscriminatorValue("Admin")
@NamedQueries({
        @NamedQuery(name = "AdminDetails.findAll", query = "SELECT m FROM AdminDetails m"),
        @NamedQuery(name = "AdminDetails.findById", query = "SELECT m FROM AdminDetails m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class AdminDetails extends Role implements Serializable {

    private static final long serialVersionUID = 1L;
}
