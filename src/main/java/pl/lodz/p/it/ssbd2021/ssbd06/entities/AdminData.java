package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_data")
@DiscriminatorValue("ADMIN")
@NamedQueries({
        @NamedQuery(name = "AdminData.findAll", query = "SELECT m FROM AdminData m"),
        @NamedQuery(name = "AdminData.findById", query = "SELECT m FROM AdminData m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class AdminData extends Role implements Serializable {

    private static final long serialVersionUID = 1L;
}
