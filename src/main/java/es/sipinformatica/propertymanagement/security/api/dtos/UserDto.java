package es.sipinformatica.propertymanagement.security.api.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NonNull
    @NotBlank
    private String phone;    
    private String firstName;
    private String lastName;
    @NonNull
    @NotBlank
    @Size(max = 50)
    private String email;
    private String dni;
    private String address;    
    @Size(min = 6, max = 40)
    private String password;
    private Boolean isEnabled;
    private LocalDateTime firstAccess;
    private LocalDateTime lastAccess;
    @Singular
    private Set<String> roles;
    @NonNull
    @NotBlank
    @Size(min = 3, max = 20)  
    private String username;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private String city;
    private String country;  
    
    public UserDto(User user) {
        String secret = "secret";
        BeanUtils.copyProperties(user, this);

        this.password = secret;
        this.roles =   getRolesUser(user);       
    }

    public static UserDto ofMobileFirstName(User user) {
        return UserDto.builder().phone(user.getPhone()).firstName(user.getFirstName()).username(user.getUsername())
        .email(user.getEmail()).build();
    }

    public static UserDto ofUser(User user) {

        Set<String> role =   getRolesUser(user);        

        return UserDto.builder().phone(user.getPhone()).firstName(user.getFirstName()).lastName(user.getLastName())
                .dni(user.getDni()).email(user.getEmail()).address(user.getAddress()).password("secret")
                .isEnabled(user.getIsEnabled()).roles(role).firstAccess(user.getFirstAccess())
                .lastAccess(user.getLastAccess()).username(user.getUsername())
                .isAccountNonExpired(user.getIsAccountNonExpired()).isAccountNonLocked(user.getIsAccountNonLocked())
                .isCredentialsNonExpired(user.getIsCredentialsNonExpired()).city(user.getCity())
                .country(user.getCountry()).build();
    }

    public void doDefault() {                 

        if (Objects.isNull(password)) {
            this.password = UUID.randomUUID().toString();
        }
        if (Objects.isNull(roles)) {
            this.roles.add(ERole.ROLE_MANAGER.name());
        }
        if (Objects.isNull(isEnabled)) {
            this.isEnabled = true;
        }
    }

    public User toUser() {
        this.doDefault();        
        this.password = new BCryptPasswordEncoder().encode(this.password);
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.getRoles().clear();
        return user;
    }

    public Set<String> RolesUserDto() {
        Set<String> rolesUserDto = this.getRoles().stream().collect(Collectors.toSet());   
        return rolesUserDto;
    }

    public static  Set<String> getRolesUser(User user) {
        Set<String> roles = user.getRoles().stream().map(Role::getName).map(ERole::name).collect(Collectors.toSet());
        return roles;
    }
}
