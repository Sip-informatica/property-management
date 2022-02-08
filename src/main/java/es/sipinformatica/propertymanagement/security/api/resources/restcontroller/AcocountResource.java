package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.api.httpserrors.MessageResponse;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.services.MailService;
import es.sipinformatica.propertymanagement.security.domain.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AcocountResource {
    private static final String REGISTER = "/register";
    private static final String ACTIVATE = "/register/activate/{token}";
    private static final String ERROR = "Error: ";
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;

    @PostMapping(REGISTER)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest,
            HttpServletRequest request)
            throws MessagingException {
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
        mailService.sendActivationEmail(user, request.getRequestURL().toString());        
        return ResponseEntity.ok(new MessageResponse(userSignupRequest.getUsername() + " registered successfully"));

    }

    @GetMapping(ACTIVATE)
    public ResponseEntity<MessageResponse> activateUser(@PathVariable String token) {
        Optional<User> user = userService.activateUser(token);
        if (!user.isPresent()) {
            throw new ResourceConflictException(ERROR + "Invalid token");
        }

        return ResponseEntity.ok(new MessageResponse("User activated successfully"));
    }
}
