package es.sipinformatica.propertymanagement.security.api.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

class UserDtoTest {
    private User user;
    Set<Role> roles = new HashSet<>();
    
    @BeforeEach
    void initData() {
      roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
      roles.add(Role.builder().name(ERole.ROLE_MANAGER).build());

      user = User.builder()
        .phone("666777888")
        .roles(roles)
        .dni("44444444Q")
        .email("email@email.com")
        .username("User-name")
        .password("password")
        .firstName("First-Name")
        .build();
    }

    @Test
    void shouldUserDto() {
        UserDto userDto = new UserDto(user);
        
        assertEquals("44444444Q", userDto.getDni());
        assertEquals("[ROLE_MANAGER, ROLE_ADMIN]", userDto.getRoles().toString());
    }

    @Test
    void shouldOfMobileFirstName(){
        UserDto userDto = UserDto.ofMobileFirstName(user);

        assertEquals("First-Name", userDto.getFirstName());
    }

    @Test
    void shouldOfUser(){
        UserDto userDto = UserDto.ofUser(user);

        assertNotEquals("password", userDto.getPassword());
        assertEquals("secret", userDto.getPassword());
    }  
    
    @Test
    void shouldDoDefault() {
        UserDto userDto = new UserDto(user);
        userDto.doDefault();
        
        assertEquals("secret", userDto.getPassword());
        assertEquals("[ROLE_MANAGER, ROLE_ADMIN]", userDto.getRoles().toString());
        assertTrue(userDto.getIsEnabled());
    }

    @Test
    void shouldToUser() {
        UserDto userDto = new UserDto(user); 
        Set<String> roles = user.getRoles().stream()
        .map(Role::getName).map(ERole::name).collect(Collectors.toSet());
       
          
               
        assertNotEquals("secret", userDto.toUser().getPassword());
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_ADMIN") && roles.contains("ROLE_MANAGER"));
        assertEquals("First-Name", userDto.getFirstName());
        assertTrue(userDto.toUser().getIsEnabled());
    }

}
