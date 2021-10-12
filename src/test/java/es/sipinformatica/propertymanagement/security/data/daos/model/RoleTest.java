package es.sipinformatica.propertymanagement.security.data.daos.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;

class RoleTest {
    private Role role;

    @Test
    void shouldSetName() {
        role = new Role(1, ERole.ROLE_CUSTOMER);

        assertEquals(ERole.ROLE_CUSTOMER, role.getName());
    }

    @Test
    void shouldBuilderRole() {
        role = Role.builder().name(ERole.ROLE_ADMIN).build();
        
        assertEquals(ERole.ROLE_ADMIN, role.getName());
    }

}
