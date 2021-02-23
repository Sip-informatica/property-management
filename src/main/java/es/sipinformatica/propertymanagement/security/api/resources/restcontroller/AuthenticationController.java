package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sipinformatica.propertymanagement.security.api.dtos.UserDetailsImpl;
import es.sipinformatica.propertymanagement.security.api.dtos.request.LoginRequest;
import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.api.dtos.response.JwtResponse;
import es.sipinformatica.propertymanagement.security.api.httpserrors.MessageResponse;
import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;
import es.sipinformatica.propertymanagement.security.domain.services.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    private static final String ERROR = "Error: ";

@PostMapping("/signin")
public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
    Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwtToken = jwtService.createJwtToken(authentication);
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    
    JwtResponse jwtResponse = new JwtResponse(
    jwtToken, 
    userDetails.getId(),
    userDetails.getUsername(),
    userDetails.getEmail(),
    roles);    
    
    return ResponseEntity.ok(jwtResponse);   

}  

@PostMapping("/signup")
public ResponseEntity<Object> registerUser(@Valid @RequestBody UserSignupRequest signupRequest){
    Boolean existsByUsername = userRepository.existsByUsername(signupRequest.getUsername());
    Boolean existsByEmail = userRepository.existsByEmail(signupRequest.getEmail());

    if (Boolean.TRUE.equals(existsByUsername)){
        throw new ResourceConflictException(ERROR + signupRequest.getUsername() + " is already in use!");      
    }
    if (Boolean.TRUE.equals(existsByEmail)){
        throw new ResourceConflictException(ERROR + signupRequest.getEmail() + " is already in use!");
    }
    
    User user = new User(signupRequest.getUsername(),
        signupRequest.getEmail(),
        passwordEncoder.encode(signupRequest.getPassword()));
    
    Set<Role> roles = new HashSet<>();    
    Role userRole = roleRepository.findByName(ERole.ROLE_MANAGER)
        .orElseThrow(()-> new ResourceNotFoundException(ERROR + ERole.ROLE_MANAGER.name() + " Role is not found"));
    roles.add(userRole);  
   
    user.setRoles(roles);
    userRepository.save(user);    

    return ResponseEntity.ok(new MessageResponse(user.getUsername() 
    + " User registered successfully, Role: " 
    + user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
    + " " + HttpStatus.CREATED ));
}

}
