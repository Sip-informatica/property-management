package es.sipinformatica.propertymanagement.security.api.dtos.request;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestTest {
    private LoginRequest loginRequest;

    @BeforeEach
    void loginRequestInit(){
        loginRequest = new LoginRequest("username", "password");
    }
    @Test
    void getUsernameTest() {
        assertTrue(loginRequest.getUsername().contentEquals("username"));
    }
    @Test
    void getPasswordTest() {
        assertTrue(loginRequest.getPassword().contentEquals("password"));
        loginRequest = LoginRequest.builder().password("passwordChanged").build();
        assertTrue(loginRequest.getPassword().contentEquals("passwordChanged"));

    }

    
    
}
