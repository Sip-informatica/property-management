package es.sipinformatica.propertymanagement.security.domain.services;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

@Service
public class AdminService {

    private UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Stream<User> readAll() {
        return this.userRepository.findAll().stream();
    }

    public void create(User user) {
        user.setFirstAccess(LocalDateTime.now());
        this.checkMobile(user.getPhone());
        this.checkEmail(user.getEmail());
        this.userRepository.save(user);
    }

    public User readByMobile(String mobile) {
        return this.userRepository.findByPhone(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("The mobile don't exist: " + mobile));

    }

    public void updateByMobile(String mobile, User user) {
        User oldUser = this.userRepository.findByPhone(mobile)
                .orElseThrow(() -> new ResourceNotFoundException("The mobile don't exist: " + mobile));
        BeanUtils.copyProperties(user, oldUser, "id", "password", "firstAccess");
        this.userRepository.save(oldUser);
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    private void checkMobile(String mobile) {
        if (this.userRepository.existsByPhone(mobile)) {
            throw new ResourceConflictException("The mobile already exists: " + mobile);
        }
    }

    private void checkEmail(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("The email already exists: " + email);
        }
    }

    public User readByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email don't exist: " + email));

    }

    public void updateByEmail(String email, User user) {
        User oldUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email don't exist: " + email));
        BeanUtils.copyProperties(user, oldUser, "id", "password", "firstAccess");
        this.userRepository.save(oldUser);
    }
}
