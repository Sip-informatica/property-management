package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import es.sipinformatica.propertymanagement.security.api.dtos.response.JwtResponse;
import es.sipinformatica.propertymanagement.security.data.daos.RoleRepository;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;
import es.sipinformatica.propertymanagement.security.domain.services.JwtService;
import es.sipinformatica.propertymanagement.security.domain.services.UserService;

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
    @Autowired
    UserService userService;

    private static final String LOGIN ="/signin";

    @PostMapping(LOGIN)
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User userLogin = userService.findUser(loginRequest.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtService.createJwtToken(authentication);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
       
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwtToken)
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .dni(userDetails.getDni())
                .roles(roles)
                .build();

        return ResponseEntity.ok(jwtResponse);

    }  

}
