package es.sipinformatica.propertymanagement.security.api.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
import lombok.Singular;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotNull
    @NotBlank
    private String mobile;
    @NotNull
    @NotBlank
    private String firstName;
    private String lastName;
    @NotNull
    @NotBlank
    private String email;
    private String dni;
    private String address;
    private String password;
    private Boolean isEnabled;
    private LocalDateTime firstAccess;
    @Singular
    private Set<String> roles;

    public UserDto(User user) {
        String secret = "secret";
        BeanUtils.copyProperties(user, this);
        
        this.password = secret;
        this.roles = user.getRoles().stream().map(Role::getName).map(ERole::name).collect(Collectors.toSet());
        
    }

    public static UserDto ofMobileFirstName(User user) {
        return UserDto.builder().mobile(user.getPhone()).firstName(user.getFirstName()).build();
    }

    public static UserDto ofUser(User user) {

        Set<String> role = user.getRoles().stream().map(Role::getName).map(ERole::name).collect(Collectors.toSet());

        return UserDto.builder().mobile(user.getPhone()).firstName(user.getFirstName()).lastName(user.getLastName())
                .dni(user.getDni()).email(user.getEmail()).address(user.getAddress()).password("secret")
                .isEnabled(user.getIsEnabled()).roles(role).firstAccess(user.getFirstAccess()).build();
    }

    public void doDefault() {
        if (Objects.isNull(password)) {
            password = UUID.randomUUID().toString();
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
        return user;
    }

}
