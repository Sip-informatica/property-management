package es.sipinformatica.propertymanagement.security.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;

import es.sipinformatica.propertymanagement.security.data.model.ERole;

@Service
public class RestClientTestService {
    private JwtService jwtService;
    private String token;
    private AuthenticationManager authenticationManager;   
   
    @Autowired
public RestClientTestService(JwtService jwtService, AuthenticationManager authenticationManager){
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;    
}

private WebTestClient login(List<GrantedAuthority> roles ,String username, String password, WebTestClient webTestClient){ 
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
    new UsernamePasswordAuthenticationToken(username, password, roles);

    Authentication authentication = authenticationManager
    .authenticate(usernamePasswordAuthenticationToken);
    
    this.token = jwtService.createJwtToken(authentication);

    return webTestClient.mutate()
    .defaultHeader("Authorization", "Bearer" + this.token)
    .build();
}

public WebTestClient loginAdmin(WebTestClient webTestClient){
    List<GrantedAuthority> grantedAuthority = new ArrayList<>();   
    grantedAuthority.add(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()));
    return this.login(grantedAuthority, "admin", "passAdmin", webTestClient);
}

public WebTestClient loginManager(WebTestClient webTestClient){
    List<GrantedAuthority> grantedAuthority = new ArrayList<>();   
    grantedAuthority.add(new SimpleGrantedAuthority(ERole.ROLE_MANAGER.name()));
    return this.login(grantedAuthority, "manager", "passManager", webTestClient);
}

public WebTestClient loginauthenticated(WebTestClient webTestClient){
    List<GrantedAuthority> grantedAuthority = new ArrayList<>();   
    grantedAuthority.add(new SimpleGrantedAuthority(ERole.ROLE_AUTHENTICATED.name()));
    return this.login(grantedAuthority, "authenticated", "passAuthenticated", webTestClient);
}

public WebTestClient loginAdminManager(WebTestClient webTestClient){
    List<GrantedAuthority> grantedAuthority = new ArrayList<>();   
    grantedAuthority.add(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()));
    grantedAuthority.add(new SimpleGrantedAuthority(ERole.ROLE_MANAGER.name()));
    return this.login(grantedAuthority, "AdminManager", "passAdminManager", webTestClient);
}

public void logout() {
    this.token = null;
}

public String getToken() {
    return token;
}
   
}
