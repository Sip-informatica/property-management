package es.sipinformatica.propertymanagement.security.data.daos.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;

class RoleTest {
    private Role role;

    @Test
    void shouldSetName() {
        role = new Role(1, ERole.ROLE_CUSTOMER);
        
        assertTrue(role.getName().equals(ERole.ROLE_CUSTOMER));
    }  
    
    @Test
    void shouldBuilderRole() {
        role = Role.builder().name(ERole.ROLE_ADMIN).build();

        assertTrue(role.getName().equals(ERole.ROLE_ADMIN));
    }
    
}
