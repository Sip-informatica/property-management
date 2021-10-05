package es.sipinformatica.propertymanagement.security.api.dtos.request;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSignupRequestTest {
    private UserSignupRequest userSignupRequest;
    
    @BeforeEach
    private void userSignupRequestInit(){
        userSignupRequest = new UserSignupRequest("username", "email", "password");        
    }
    @Test
    void shouldGetUsername(){
        assertTrue(userSignupRequest.getUsername().contentEquals("username"));
    }
    @Test
    void shouldGetpassword(){
        assertTrue(userSignupRequest.getPassword().contentEquals("password"));
    }
    @Test
    void shouldGetRole(){
        assertNull(userSignupRequest.getRoles());
    }
}
