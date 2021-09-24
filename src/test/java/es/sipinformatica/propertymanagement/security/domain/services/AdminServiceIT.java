package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AdminServiceIT {

    private User userBuilder;
    Set<Role> roles = new HashSet<>();
    @Autowired
    private AdminService adminService;

    @BeforeEach
    void userInit() {
        this.roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
        this.roles.add(Role.builder().name(ERole.ROLE_CUSTOMER).build());

        this.userBuilder = User.builder().username("administradorTest").email("emailTest@sip.es").phone("phoneTest")
                .dni("dniTest").password("password").roles(roles).build();
    }

    @Test
    void shouldCreateUser() {
        Set<String> rolesString = new HashSet<>();
        userBuilder.getRoles().clear();

        rolesString.add("ERROR_ADMIN");
        rolesString.add("ROLE_ADMIN");
        adminService.mapRoles(userBuilder, rolesString);
        Set<String> roles = userBuilder.getRoles().stream().map(Role::getName).map(ERole::name)
                .collect(Collectors.toSet());

        assertFalse(roles.contains("ERROR_ADMIN"));
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertFalse(roles.contains("ROLE_AUTHENTICATED"));

        rolesString.clear();
        assertFalse(roles.contains("ROLE_MANAGER"));
    }

}
