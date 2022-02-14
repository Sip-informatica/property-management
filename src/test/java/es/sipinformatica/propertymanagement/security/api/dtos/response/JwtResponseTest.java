package es.sipinformatica.propertymanagement.security.api.dtos.response;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtResponseTest {
    private List<String> roles = new ArrayList<>();
    private JwtResponse jwtResponse;

    @BeforeEach
    void jwtResponseInit() {
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_MANAGER");
        jwtResponse = JwtResponse.builder()
                .token("token")
                .username("username")
                .email("email@sip.es")
                .roles(roles).build();

    }

    @Test
    void shouldGetToken() {
        assertTrue(jwtResponse.getToken().contentEquals("token"));
        jwtResponse.setToken("tokenChange");
        assertFalse(jwtResponse.getToken().contentEquals("token"));
    }

    @Test
    void shouldGetUsername() {
        assertTrue(jwtResponse.getUsername().contentEquals("username"));
        jwtResponse.setUsername("usernameChange");
        assertFalse(jwtResponse.getUsername().contentEquals("username"));
    }

    @Test
    void shouldGetEmail() {
        assertTrue(jwtResponse.getEmail().contentEquals("email@sip.es"));
        jwtResponse.setEmail("email@sip.esChange");
        assertFalse(jwtResponse.getEmail().contentEquals("email@sip.es"));
    }

    @Test
    void shouldGetRoles() {
        assertTrue(jwtResponse.getRoles().contains("ROLE_ADMIN"));
        roles.removeAll(roles);
        assertFalse(jwtResponse.getEmail().contentEquals("ROLE_ADMIN"));
    }
}
