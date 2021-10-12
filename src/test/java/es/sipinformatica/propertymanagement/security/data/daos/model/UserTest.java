package es.sipinformatica.propertymanagement.security.data.daos.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sipinformatica.propertymanagement.security.data.model.User;

class UserTest {
    private User user;
    private User userBuilder;

    @BeforeEach
    void userInit(){
        this.user = new User("username", "email@sip.es", "password");
        this.userBuilder = User.builder()
        .username("usernamebuilder")
        .email("emailbuilder@sip.es")
        .password("password")
        .phone("123456729")
        .isAccountNonExpired(true)
        .isAccountNonLocked(true)
        .isCredentialsNonExpired(true)
        .isEnabled(true)
        .build();
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

    @Test
    void shouldNewUserBuilder(){
        assertTrue(userBuilder.getUsername().contentEquals("usernamebuilder"));
        assertTrue(userBuilder.getIsEnabled());
    }
}
