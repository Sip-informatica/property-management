package es.sipinformatica.propertymanagement.security.data.daos.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sipinformatica.propertymanagement.security.data.model.User;

class UserTest {
    private User user;

    @BeforeEach
    void userInit(){
        this.user = new User("username", "email@sip.es", "password");
    }
    
    @Test
    void shouldGetUsername(){
        assertTrue(user.getUsername().contentEquals("username"));
    }
    @Test
    void shouldGetEmail(){
        assertTrue(user.getEmail().contains("@"));
    }

    @Test
    void shouldGetTrueMethod(){
        assertTrue(user.getIsAccountNonExpired());
        assertTrue(user.getIsAccountNonLocked());
        assertTrue(user.getIsCredentialsNonExpired());
        assertTrue(user.getIsEnabled());
    }
}
