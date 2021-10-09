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

import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
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
        @Autowired
        private UserRepository userRepository;
        private User userBuilderCreate;

        @BeforeEach
        void userInit() {
                this.roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
                this.roles.add(Role.builder().name(ERole.ROLE_CUSTOMER).build());

                this.userBuilderCreate = User.builder().email("adminServiceemailCreateTest@sip.es").username("   ")
                                .phone("adminServicephoneCreateTest").dni("adminServicedniCreateTest").password("password").roles(roles)
                                .isEnabled(false).build();
                this.userBuilder = User.builder().username("adminServiceusernameRoles").email("adminServiceemailTest@sip.es").phone("adminServicephoneTest")
                                .dni("adminServicedniTest").password("password").roles(roles).build();
        }

       @Test
        void shouldMapRoles() {
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
                adminService.mapRoles(userBuilder, rolesString);
                Set<String> rolesClear = userBuilder.getRoles().stream().map(Role::getName).map(ERole::name)
                                .collect(Collectors.toSet());
                assertTrue(rolesClear.contains("ROLE_MANAGER"));
        }

        @Test
        void shouldCreateUser() {

                Set<String> rolesString = new HashSet<>();
                rolesString.add("ROLE_ADMIN");
                adminService.create(userBuilderCreate, rolesString);
                User userTest = userRepository.findByEmail("adminServiceemailCreateTest@sip.es").orElseThrow();
                Set<String> roles = userTest.getRoles().stream().map(Role::getName).map(ERole::name)
                                .collect(Collectors.toSet());

                assertTrue(roles.contains("ROLE_ADMIN"));
                assertTrue(userTest.getUsername().contentEquals("adminServiceemailCreateTest@sip.es"));
                assertTrue(userTest.getIsAccountNonLocked());
                assertFalse(userTest.getIsEnabled());

                userRepository.delete(userTest);
        }

}
