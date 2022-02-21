package es.sipinformatica.propertymanagement.security.domain.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        String activationKey = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());

        deleteExpiredUsers(userSignupRequest);
        Set<Role> rol = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_MANAGER)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                ERROR + ERole.ROLE_MANAGER.name() + " Role is not found"));
        rol.add(role);
        User newUser = User.builder()
                .username(userSignupRequest.getUsername().toLowerCase())
                .email(userSignupRequest.getEmail())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))
                .dni(userSignupRequest.getDni().toUpperCase())
                .phone(userSignupRequest.getPhone()).roles(rol).isEnabled(false)
                .activationKey(activationKey).firstAccess(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        return newUser;

    }

    private void deleteExpiredUsers(UserSignupRequest userSignupRequest) {
        User findDeleteUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(userSignupRequest.getUsername())
                        || user.getEmail().equals(userSignupRequest.getEmail())
                        || user.getDni().equals(userSignupRequest.getDni())
                        || user.getPhone().equals(userSignupRequest.getPhone()))
                .findFirst().orElse(null);
        if (findDeleteUser != null) {
            userRepository.delete(findDeleteUser);
        }
    }

    public User findUser(String login) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(login)
                        || user.getEmail().equals(login)
                        || user.getDni().equals(login)
                        || user.getPhone().equals(login))
                .findFirst().orElse(null);

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

    @Scheduled(cron = "@weekly")
    public void deleteExpiredUsers() {
        log.info("Tarea usando expresiones cron - " + LocalDateTime.now());
        userRepository.findAll().forEach(user -> {
            if (user.getActivationKey() != null) {
                userRepository.delete(user);
            }
        });
    }

}
