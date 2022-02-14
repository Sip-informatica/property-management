package es.sipinformatica.propertymanagement.security.domain.services;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

@Service
public class AdminService {
    private static final String[] IGNORE_PROPERTIES = { "id", "password", "firstAccess", "email" };

    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private static final String ERROR = "Error: ";

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Stream<User> readAll() {
        return this.userRepository.findAll().stream();
    }

    public void create(User user, Set<String> roles) {
        this.mapRoles(user, roles);
        this.checkMobile(user.getPhone());
        this.checkEmail(user.getEmail());
        this.checkDni(user.getDni());
        this.checkUsername(user.getUsername());
        this.fillUsername(user);
        this.checkIsBoleanAccount(user);
        user.setFirstAccess(LocalDateTime.now());
        user.setLastAccess(LocalDateTime.now());
        userRepository.save(user);
    }

    public User mapRoles(User user, Set<String> roles) {
        Set<String> eRoleValue = Stream.of(ERole.values()).map(ERole::name).collect(Collectors.toSet());
        Set<Role> rolesOfUser = eRoleValue.stream().filter(roles::contains).map(ERole::valueOf)
                .map(role -> roleRepository.findByName(role)
                        .orElseThrow(() -> new ResourceNotFoundException(ERROR + role.name() + " Role is not found")))
                .collect(Collectors.toSet());

        if (rolesOfUser.isEmpty()) {
            rolesOfUser = Stream.of(ERole.ROLE_MANAGER)
                    .map(role -> roleRepository.findByName(role).orElseThrow(
                            () -> new ResourceNotFoundException(ERROR + role.name() + " Role is not found")))
                    .collect(Collectors.toSet());
        }
        user.setRoles(rolesOfUser);
        return user;
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public User readByMobile(String mobile) {
        return this.userRepository.findByPhone(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("The mobile don't exist: " + mobile));

    }

    public void updateByMobile(String mobile, User user, Set<String> roles) {
        User oldUser = this.userRepository.findByPhone(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("The mobile don't exist: " + mobile));
        if (!user.getEmail().equals(oldUser.getEmail())) {
            this.checkEmail(user.getEmail());
        }
        if (!user.getDni().equals(oldUser.getDni())) {
            this.checkDni(user.getDni());
        }
        if (!user.getUsername().equals(oldUser.getUsername())) {
            this.checkUsername(user.getUsername());
        }
        this.mapRoles(user, roles);
        BeanUtils.copyProperties(user, oldUser, IGNORE_PROPERTIES);
        oldUser.setLastAccess(LocalDateTime.now());
        this.userRepository.save(oldUser);
    }

    private void checkMobile(String mobile) {
        if (Boolean.TRUE.equals(this.userRepository.existsByPhone(mobile))) {
            throw new ResourceConflictException("The mobile already exists: " + mobile);
        }
    }

    private void checkEmail(String email) {
        if (Boolean.TRUE.equals(this.userRepository.existsByEmail(email))) {
            throw new ResourceConflictException("The email already exists: " + email);
        }
    }

    public User readByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email don't exist: " + email));

    }

    public void updateByEmail(String email, User user, Set<String> roles) {
        User oldUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email don't exist: " + email));
        if (!user.getPhone().equals(oldUser.getPhone())) {
            this.checkMobile(user.getPhone());
        }
        if (!user.getDni().equals(oldUser.getDni())) {
            this.checkDni(user.getDni());
        }
        if (!user.getUsername().equals(oldUser.getUsername())) {
            this.checkUsername(user.getUsername());
        }
        this.mapRoles(user, roles);
        BeanUtils.copyProperties(user, oldUser, IGNORE_PROPERTIES);
        oldUser.setLastAccess(LocalDateTime.now());
        this.userRepository.save(oldUser);
    }

    private void checkDni(String dni) {
        UtilService.validateNieNifNifBusiness(dni);
        if (Boolean.TRUE.equals(this.userRepository.existsByDni(dni))) {
            throw new ResourceConflictException("The dni already exists: " + dni);
        }
    }

    public User readByDni(String dni) {
        return this.userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("The Dni don't exist: " + dni));

    }

    public void updateByDni(String dni, User user, Set<String> roles) {
        User oldUser = this.userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("The DNI don't exist: " + dni));
        if (!user.getPhone().equals(oldUser.getPhone())) {
            this.checkMobile(user.getPhone());
        }
        if (!user.getEmail().equals(oldUser.getEmail())) {
            this.checkEmail(user.getEmail());
        }
        if (!user.getUsername().equals(oldUser.getUsername())) {
            this.checkUsername(user.getUsername());
        }
        this.mapRoles(user, roles);
        BeanUtils.copyProperties(user, oldUser, IGNORE_PROPERTIES);
        oldUser.setLastAccess(LocalDateTime.now());
        this.userRepository.save(oldUser);
    }

    private void checkUsername(String username) {
        if (Boolean.TRUE.equals(this.userRepository.existsByUsername(username))) {
            throw new ResourceConflictException("The Username already exists: " + username);
        }
    }

    public User readByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("The username don't exist: " + username));

    }

    public void updateByUsername(String username, User user, Set<String> roles) {
        User oldUser = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("The username don't exist: " + username));
        if (!user.getPhone().equals(oldUser.getPhone())) {
            this.checkMobile(user.getPhone());
        }
        if (!user.getDni().equals(oldUser.getDni())) {
            this.checkDni(user.getDni());
        }
        if (!user.getEmail().equals(oldUser.getEmail())) {
            this.checkEmail(user.getEmail());
        }
        this.mapRoles(user, roles);
        BeanUtils.copyProperties(user, oldUser, IGNORE_PROPERTIES);
        oldUser.setLastAccess(LocalDateTime.now());
        this.userRepository.save(oldUser);
    }

    private User fillUsername(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getUsername().trim().isEmpty()) {
            user.setUsername(user.getEmail());
        }
        return user;
    }

    private User checkIsBoleanAccount(User user) {
        if (user.getIsAccountNonExpired() == null) {
            user.setIsAccountNonExpired(true);
        }
        if (user.getIsAccountNonLocked() == null) {
            user.setIsAccountNonLocked(true);
        }
        if (user.getIsCredentialsNonExpired() == null) {
            user.setIsCredentialsNonExpired(true);
        }
        if (user.getIsEnabled() == null) {
            user.setIsEnabled(true);
        }
        return user;

    }
}
