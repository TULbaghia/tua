package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_data")
@NamedQueries({
        @NamedQuery(name = "ClientData.findAll", query = "SELECT m FROM ClientData m"),
        @NamedQuery(name = "ClientData.findById", query = "SELECT m FROM ClientData m WHERE m.id = :id")})
@NoArgsConstructor
@ToString(callSuper = true)
public class ClientData extends Role implements Serializable {

    private static final long serialVersionUID = 1L;

}