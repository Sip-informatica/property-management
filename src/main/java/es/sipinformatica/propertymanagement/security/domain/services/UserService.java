package es.sipinformatica.propertymanagement.security.domain.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    private static final String ERROR = "Error: ";
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    public User registerUser(UserSignupRequest userSignupRequest) {
        Set<Role> rol = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_MANAGER)
                .orElseThrow(
                        () -> new ResourceNotFoundException(ERROR + ERole.ROLE_MANAGER.name() + " Role is not found"));
        rol.add(role);
        User newUser = User.builder()
                .username(userSignupRequest.getUsername().toLowerCase())
                .email(userSignupRequest.getEmail())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))
                .dni(userSignupRequest.getDni().toUpperCase())
                .phone(userSignupRequest.getPhone()).roles(rol).isEnabled(false)
                .activationKey(RandomStringUtils.randomAlphanumeric(20)).firstAccess(LocalDateTime.now())
                .build();
        userRepository.save(newUser);

        return newUser;
    }

    public Optional<User> activateUser(String token) {
        return userRepository.findByActivationKey(token)
                .map(user -> {
                    user.setActivationKey(null);
                    user.setIsEnabled(true);
                    user.setIsAccountNonExpired(true);
                    user.setIsAccountNonLocked(true);
                    user.setIsCredentialsNonExpired(true);
                    userRepository.save(user);
                    return user;
                });
    }

}
