package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtServiceTest {
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;

    private  MockHttpServletRequest request;   
    private String jwtToken;
    private static final String  TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjE0MjUxMjUxLCJleHAiOjE2MTQzMzc2NTF9.qesgejQAc0Z_g_F3ANyOnMRTicBPmabAqOjWb5INObwXqBmrCSbmqORzRBWqi0x1wUsVuGBAZsWmj2El3cShEQ";
    
    @BeforeEach
    private void createJwtToken() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                "admin", "passAdmin", roles);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        jwtToken = jwtService.createJwtToken(authentication);
        log.info("Token: " + jwtToken);

        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer" + TOKEN);       

    }

    @Test
    void shouldCreateToken() {
        assertNotNull(jwtToken);
    }

    @Test
    void shouldGetUsernameFromJwtToken() {
        assertTrue(jwtService.getUserNameFromJwtToken(jwtToken).contentEquals("admin"));
        assertFalse(jwtService.getUserNameFromJwtToken(jwtToken).contentEquals("adminFalse"));
    }

    @Test
    void shouldValidateJwtToken() {
        assertTrue(jwtService.validateJwtToken(jwtToken));
        assertFalse(jwtService.validateJwtToken(jwtToken + "False"));
        assertTrue(jwtToken.contains("."));
        assertEquals(20, jwtToken.indexOf("."));  
        assertEquals(2, StringUtils.countOccurrencesOf(jwtToken, ".")); 
        assertDoesNotThrow(() -> {jwtService.validateJwtToken("jwtMalformed");});
        assertDoesNotThrow(() -> {jwtService.validateJwtToken("");});  
        assertDoesNotThrow(() -> {jwtService.validateJwtToken(TOKEN);});            
    }
    @Test
    void shouldParseJwt() { 
        assertDoesNotThrow(() -> {jwtService.parseJwt(request);});
    }
    
}
