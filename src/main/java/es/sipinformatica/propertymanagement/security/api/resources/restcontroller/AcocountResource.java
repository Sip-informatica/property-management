package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.services.MailService;
import es.sipinformatica.propertymanagement.security.domain.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AcocountResource {
    private static final String ERROR = "Error: ";
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) throws MessagingException {
        userRepository.findByUsername(userSignupRequest.getUsername())
                .ifPresent(user -> {
                    throw new ResourceConflictException(ERROR + "User already exists");
                });
        userRepository.findByEmail(userSignupRequest.getEmail())
                .ifPresent(user -> {
                    throw new ResourceConflictException(ERROR + "Email already exists");
                });
        userRepository.findByPhone(userSignupRequest.getPhone())
                .ifPresent(user -> {
                    throw new ResourceConflictException(ERROR + "Phone already exists");
                });
        userRepository.findByDni(userSignupRequest.getDni()).ifPresent(user -> {
            throw new ResourceConflictException(ERROR + "NIF already exists");
        });
        User user = userService.registerUser(userSignupRequest);
        mailService.sendActivationEmail(user);

    }
}
