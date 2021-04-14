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
        @UniqueConstraint(name = "uq_account_login", columnNames = {"login"}),
        @UniqueConstraint(name = "uq_account_contactNumber", columnNames = {"contact_number"})
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_account_id")
    @SequenceGenerator(name = "seq_account_id")
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @NotNull
    @Size(min = 6, max = 127)
    @Column(name = "login", nullable = false)
    private String login;

    @Setter
    @NotNull
    @Size(min = 64, max = 64)
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Setter
    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Setter
    @NotNull
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Setter
    @NotNull
    @Size(min = 3, max = 31)
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Setter
    @NotNull
    @Size(min = 2, max = 31)
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Setter
    @Size(min = 2, max = 2)
    @Column(name = "language")
    private String language;

    @Setter
    @Size(min = 9, max = 15)
    @Column(name = "contact_number")
    private String contactNumber;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_successful_login_date")
    private Date lastSuccessfulLoginDate;

    @Setter
    @Column(name = "last_successful_login_ip_address")
    private Inet4Address lastSuccessfulLoginIpAddress;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_failed_login_date")
    private Date lastFailedLoginDate;

    @Setter
    @Column(name = "last_failed_login_ip_address")
    private Inet4Address lastFailedLoginIpAddress;

    @Setter
    @Min(value = 0)
    @Column(name = "failed_login_attempts_counter", columnDefinition = "integer default 0")
    private Integer failedLoginAttemptsCounter = 0;

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
