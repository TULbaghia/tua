package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_details")
@DiscriminatorValue("Client")
@NamedQueries({
        @NamedQuery(name = "ClientDetails.findAll", query = "SELECT m FROM ClientDetails m"),
        @NamedQuery(name = "ClientDetails.findById", query = "SELECT m FROM ClientDetails m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class ClientDetails extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

}