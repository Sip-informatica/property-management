package es.sipinformatica.propertymanagement.security.data.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    // digit + lowercase char + uppercase char + punctuation + symbol
    @Transient 
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    @NonNull
    @Column(unique = true)
    private String dni;   
    @NonNull
    @Column(unique = true, nullable = false)
    private String username;
    @NonNull
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;    
    @NonNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;     
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;
    @ManyToMany
    @JoinTable(
         name = "user_roles",
         joinColumns = @JoinColumn( name = "user_id" ),
         inverseJoinColumns = @JoinColumn( name= "role_id" ))
    private final Set<Role> roles = new HashSet<>();
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String country;
    private LocalDateTime firstAccess;
    private LocalDateTime lastAccess;
}
