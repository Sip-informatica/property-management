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
    // digit + lowercase char + uppercase char + punctuation + symbol
    /*@Transient 
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";*/
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    @Column(unique = true)
    private String dni;        
    @Column(unique = true)
    private String username;
    @NotBlank
    @NonNull
    //@Pattern(regexp = PASSWORD_PATTERN)
    private String password;    
    @NotBlank
    @NonNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;     
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;       
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
         name = "user_roles",
         joinColumns = @JoinColumn( name = "user_id" ),
         inverseJoinColumns = @JoinColumn( name= "role_id" ))
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
		this.isEnabled = true;       
    }


}
