package es.sipinformatica.propertymanagement.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import es.sipinformatica.propertymanagement.security.configuration.TestConfig;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.domain.services.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestConfig
public class JwtServiceTest {
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    private String jwtToken;

    @BeforeEach
    private void createJwtToken() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                "admin", "passAdmin", roles);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        jwtToken = jwtService.createJwtToken(authentication);
        log.info("Token: " + jwtToken);

    }

    @Test
    public void shouldCreateToken() {
        assertNotNull(jwtToken);
    }

    @Test
    public void shouldGetUsernameFromJwtToken() {
        assertTrue(jwtService.getUserNameFromJwtToken(jwtToken).contentEquals("admin"));
        assertFalse(jwtService.getUserNameFromJwtToken(jwtToken).contentEquals("adminFalse"));
    }

    @Test
    public void shouldValidateJwtToken() {
        assertTrue(jwtService.validateJwtToken(jwtToken));
        assertFalse(jwtService.validateJwtToken(jwtToken + "False"));
        assertTrue(jwtToken.contains("."));
        assertEquals(20, jwtToken.indexOf("."));  
        assertEquals(2, StringUtils.countOccurrencesOf(jwtToken, "."));     
    }
    
}
