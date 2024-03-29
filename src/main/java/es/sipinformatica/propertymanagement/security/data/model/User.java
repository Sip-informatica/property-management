package es.sipinformatica.propertymanagement.security.data.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.sipinformatica.propertymanagement.security.api.dtos.validator.ValidateNieNifNifBusiness;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ValidateNieNifNifBusiness
    @Column(unique = true)
    private String dni;
    @Column(unique = true)
    private String username;
    @NotBlank
    @NonNull
    private String password;
    @NotBlank
    @NonNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;
    private LocalDateTime resetDate;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    @NonNull
    @Column(nullable = false)
    @Builder.Default
    private Boolean isEnabled = false;
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phone;
    private String address;
    private String city;
    private String country;
    private LocalDateTime firstAccess;
    private LocalDateTime lastAccess;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = false;
    }
}
