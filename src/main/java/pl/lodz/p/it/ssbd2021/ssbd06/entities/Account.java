package pl.lodz.p.it.ssbd2021.ssbd06.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.ThemeColor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Account.CONTACT_NUMBER_CONSTRAINT;
import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Account.LOGIN_CONSTRAINT;
import static pl.lodz.p.it.ssbd2021.ssbd06.entities.Account.EMAIL_CONSTRAINT;

@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(name = LOGIN_CONSTRAINT, columnNames = {"login"}),
        @UniqueConstraint(name = CONTACT_NUMBER_CONSTRAINT, columnNames = {"contact_number"}),
        @UniqueConstraint(name = EMAIL_CONSTRAINT, columnNames = {"email"})
}, indexes = {
        @Index(name = "ix_account_created_by", columnList = "created_by"),
        @Index(name = "ix_account_modified_by", columnList = "modified_by")
})
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
        @NamedQuery(name = "Account.findByNewEmail", query = "SELECT a FROM Account a WHERE a.newEmail = :newEmail"),
        @NamedQuery(name = "Account.findByEnabled", query = "SELECT a FROM Account a WHERE a.enabled = :enabled"),
        @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed"),
        @NamedQuery(name = "Account.findByContactNumber",
                query = "SELECT a FROM Account a WHERE a.contactNumber = :contactNumber"),
        @NamedQuery(name = "Account.findByLastSuccessfulLoginIpAddress",
                query = "SELECT a FROM Account a WHERE a.lastSuccessfulLoginIpAddress = :lastSuccessfulLoginIpAddress"),
        @NamedQuery(name = "Account.findByLastFailedLoginIpAddress",
                query = "SELECT a FROM Account a WHERE a.lastFailedLoginIpAddress = :lastFailedLoginIpAddress"),
        @NamedQuery(name = "Account.findUnverified",
                query = "SELECT a FROM Account a WHERE a.confirmed = false AND a.creationDate < :date"),
        @NamedQuery(name = "Account.findUnverifiedBetweenDate",
                query = "SELECT a FROM Account a WHERE a.confirmed = false AND a.creationDate BETWEEN :dateBefore AND :dateAfter " +
                        "AND a NOT IN (SELECT p.account FROM PendingCode p WHERE used = false AND codeType = 0)")
})
@NoArgsConstructor
@Getter
public class Account extends AbstractEntity implements Serializable {

    public static final String LOGIN_CONSTRAINT = "uk_account_login";

    public static final String CONTACT_NUMBER_CONSTRAINT = "uk_account_contact_number";

    public static final String EMAIL_CONSTRAINT = "uk_account_email";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_account_id")
    @SequenceGenerator(name = "seq_account_id", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Setter
    @NotNull
    @Login
    @Column(name = "login", nullable = false)
    private String login;

    @Setter
    @NotNull
    @UserEmail
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @UserEmail
    @Column(name = "new_email")
    private String newEmail = null;

    @Setter
    @NotNull
    @EntityPassword
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
    @Firstname
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Setter
    @NotNull
    @Lastname
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Setter
    @Language
    @Column(name = "language")
    private String language;

    @Setter
    @ContactNumber
    @Column(name = "contact_number")
    private String contactNumber;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_successful_login_date")
    private Date lastSuccessfulLoginDate;

    @Setter
    @Column(name = "last_successful_login_ip_address")
    private String lastSuccessfulLoginIpAddress;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_failed_login_date")
    private Date lastFailedLoginDate;

    @Setter
    @Column(name = "last_failed_login_ip_address")
    private String lastFailedLoginIpAddress;

    @Setter
    @Min(value = 0)
    @Column(name = "failed_login_attempts_counter", columnDefinition = "integer default 0")
    private Integer failedLoginAttemptsCounter = 0;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enable_modification_date")
    private Date enableModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "enable_modification_by")
    private Account enableModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirm_modification_date")
    private Date confirmModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "confirm_modification_by")
    private Account confirmModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "email_modification_date")
    private Date emailModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "email_modification_by")
    private Account emailModificationBy;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "password_modification_date")
    private Date passwordModificationDate;

    @Setter
    @OneToOne
    @JoinColumn(name = "password_modification_by")
    private Account passwordModificationBy;


    @Setter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Booking> bookingList = new HashSet<>();

    @Setter
    @Column(name = "theme_color", nullable = false)
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private ThemeColor themeColor = ThemeColor.LIGHT;

    @Setter
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "account")
    private Set<Role> roleList = new HashSet<>();

    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            mappedBy = "account", orphanRemoval = true)
    private Set<PendingCode> pendingCodeList = new HashSet<>();

    public Account(String login, String password, boolean enabled, boolean confirmed, String firstname,
                   String lastname) {
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
    public Set<Booking> getBookingList() {
        return bookingList;
    }

    @XmlTransient
    public Set<Role> getRoleList() {
        return roleList;
    }

    @XmlTransient
    public Set<PendingCode> getPendingCodeList() {
        return pendingCodeList;
    }

    @Override public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}
