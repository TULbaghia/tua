package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2021.ssbd06.common.AbstractEntity;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"login"}),
        @UniqueConstraint(columnNames = {"contactNumber"})
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
    @NamedQuery(name = "Account.findByEnabled", query = "SELECT a FROM Account a WHERE a.enabled = :enabled"),
    @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed"),
    @NamedQuery(name = "Account.findByContactNumber", query = "SELECT a FROM Account a WHERE a.contactNumber = :contactNumber"),
    @NamedQuery(name = "Account.findByLastSuccessfulLoginIpAddress", query = "SELECT a FROM Account a WHERE a.lastSuccessfulLoginIpAddress = :lastSuccessfulLoginIpAddress"),
    @NamedQuery(name = "Account.findByLastFailedLoginIpAddress", query = "SELECT a FROM Account a WHERE a.lastFailedLoginIpAddress = :lastFailedLoginIpAddress"),
})
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
public class Account extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Setter
    @NotNull
    @Size(min = 6, max = 127)
    @Column(nullable = false)
    private String login;

    @Setter
    @NotNull
    @Size(min = 30, max = 64)
    @Column(nullable = false)
    private String password;

    @Setter
    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    @Setter
    @NotNull
    @Column(nullable = false)
    private boolean confirmed;

    @Setter
    @NotNull
    @Size(min = 3, max = 31)
    @Column(nullable = false)
    private String firstname;

    @Setter
    @NotNull
    @Size(min = 2, max = 31)
    @Column(nullable = false)
    private String lastname;

    @Setter
    @Size(min = 2, max = 2)
    private String language;

    @Setter
    @Size(min = 9, max = 15)
    private String contactNumber;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSuccessfulLoginDate;

    @Setter
    private Inet4Address lastSuccessfulLoginIpAddress;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFailedLoginDate;

    @Setter
    private Inet4Address lastFailedLoginIpAddress;

    @Setter
    @Min(value = 0)
    @Column(columnDefinition = "integer default 0")
    private Integer failedLoginAttemptsCounter;

    @Setter
    @OneToMany(mappedBy = "account")
    private List<Booking> bookingList;

    @Setter
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "account")
    private List<Role> roleList;

    @Setter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<PendingCode> pendingCodeList;

    public Account(String login, String password, boolean enabled, boolean confirmed, String firstname, String lastname) {
        this.login = login;
        this.password = password;
        this.enabled = enabled;
        this.confirmed = confirmed;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }


    @XmlTransient
    public List<Booking> getBookingList() {
        return bookingList;
    }

    @XmlTransient
    public List<Role> getRoleList() {
        return roleList;
    }

    @XmlTransient
    public List<PendingCode> getPendingCodeList() {
        return pendingCodeList;
    }
}
