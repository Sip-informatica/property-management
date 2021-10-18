package es.sipinformatica.propertymanagement.security.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

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
        private User userBuilderUpdate;

        @BeforeEach
        void userInit() {
                this.roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
                this.roles.add(Role.builder().name(ERole.ROLE_CUSTOMER).build());

                this.userBuilderCreate = User.builder().email("adminServiceemailCreateTest@sip.es").username("   ")
                                .phone("adminServicephoneCreateTest").dni("adminServicedniCreateTest")
                                .password("password").roles(roles).isEnabled(false).build();
                this.userBuilder = User.builder().username("adminServiceusernameRoles")
                                .email("adminServiceemailTest@sip.es").phone("adminServicephoneTest")
                                .dni("adminServicedniTest").password("password").roles(roles).build();
                this.userBuilderUpdate = User.builder().email("adminServiceemailCreateTest@sip.es")
                                .username("adminServiceemailCreateTest@sip.es").phone("adminServicephoneCreateTest")
                                .dni("adminServicedniCreateTest").password("password").roles(roles).isEnabled(false)
                                .build();
        }

        private void createUser() {
                Set<String> rolesString = new HashSet<>();
                rolesString.add("ROLE_ADMIN");
                userBuilderCreate.setIsAccountNonExpired(null);
                userBuilderCreate.setIsAccountNonLocked(null);
                userBuilderCreate.setIsCredentialsNonExpired(null);
                userBuilderCreate.setIsEnabled(null);

                adminService.create(userBuilderCreate, rolesString);
                adminService.create(userBuilder, rolesString);
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

                createUser();
                userRepository.delete(userBuilder);
                assertTrue(userBuilderCreate.getIsAccountNonExpired());
                assertTrue(userBuilderCreate.getIsAccountNonLocked());
                assertTrue(userBuilderCreate.getIsCredentialsNonExpired());
                assertTrue(userBuilderCreate.getIsEnabled());

                User userTest = userRepository.findByEmail("adminServiceemailCreateTest@sip.es").orElseThrow();
                Set<String> roles = userTest.getRoles().stream().map(Role::getName).map(ERole::name)
                                .collect(Collectors.toSet());

                assertTrue(roles.contains("ROLE_ADMIN"));
                assertTrue(userTest.getUsername().contentEquals("adminServiceemailCreateTest@sip.es"));
                assertTrue(userTest.getIsAccountNonLocked());
                assertTrue(userTest.getIsEnabled());

                userRepository.delete(userTest);
        }

        @Test
        void shouldUpdateByMobile() {

                createUser();

                assertThrows(ResourceNotFoundException.class,
                                () -> adminService.updateByMobile("NoAdminServicephoneTest", userBuilderCreate),
                                "The mobile don't exist: adminServicephoneTest");
                assertDoesNotThrow(() -> adminService.updateByMobile("adminServicephoneCreateTest", userBuilderCreate));

                this.userBuilderUpdate.setEmail("adminServiceemailTest@sip.es");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByMobile("adminServicephoneCreateTest", userBuilderUpdate),
                                "The email already exists: adminServiceemailTest@sip.es");

                this.userBuilderUpdate.setEmail("adminServiceemailCreateTest@sip.es");
                this.userBuilderUpdate.setDni("adminServicedniTest");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByMobile("adminServicephoneCreateTest", userBuilderUpdate),
                                "The dni already exists: adminServicedniTest");

                this.userBuilderUpdate.setDni("adminServicedniCreateTest");
                this.userBuilderUpdate.setUsername("adminServiceusernameRoles");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByMobile("adminServicephoneCreateTest", userBuilderUpdate),
                                "The Username already exists: adminServiceusernameRoles");

                userRepository.delete(userBuilderCreate);
                userRepository.delete(userBuilder);

        }

        @Test
        void shouldUpdateByEmail() {

                createUser();
                Set<String> rol = new HashSet<String>();

                assertThrows(ResourceNotFoundException.class,
                                () -> adminService.updateByEmail("emailNotExist@s.es", userBuilderCreate, rol),
                                "The email don't exist: emailNotExist@s.es");
                assertDoesNotThrow(() -> adminService.updateByEmail("adminServiceemailCreateTest@sip.es",
                                userBuilderCreate, rol));

                this.userBuilderUpdate.setPhone("adminServicephoneTest");
                assertThrows(ResourceConflictException.class, () -> adminService
                                .updateByEmail("adminServiceemailCreateTest@sip.es", userBuilderUpdate, rol),
                                "The mobile already exists: adminServicephoneTest");

                this.userBuilderUpdate.setPhone("adminServicephoneCreateTest");
                this.userBuilderUpdate.setDni("adminServicedniTest");
                assertThrows(ResourceConflictException.class, () -> adminService
                                .updateByEmail("adminServiceemailCreateTest@sip.es", userBuilderUpdate, rol),
                                "The dni already exists: adminServicedniTest");

                this.userBuilderUpdate.setDni("adminServicedniCreateTest");
                this.userBuilderUpdate.setUsername("adminServiceusernameRoles");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByEmail("adminServiceemailCreateTest@sip.es",
                                                userBuilderUpdate, rol),
                                "The Username already exists: adminServiceusernameRoles");

                userRepository.delete(userBuilderCreate);
                userRepository.delete(userBuilder);

        }

        @Test
        void shouldUpdateByDni() {

                createUser();

                assertThrows(ResourceNotFoundException.class,
                                () -> adminService.updateByDni("dni12345", userBuilderCreate),
                                "The dni don't exist: dni12345");
                assertDoesNotThrow(() -> adminService.updateByDni("adminServicedniCreateTest", userBuilderCreate));

                this.userBuilderUpdate.setPhone("adminServicephoneTest");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByDni("adminServicedniCreateTest", userBuilderUpdate),
                                "The mobile already exists: adminServicephoneTest");

                this.userBuilderUpdate.setPhone("adminServicephoneCreateTest");
                this.userBuilderUpdate.setEmail("adminServiceemailTest@sip.es");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByDni("adminServicedniCreateTest", userBuilderUpdate),
                                "The email already exists: adminServiceemailTest@sip.es");

                this.userBuilderUpdate.setEmail("adminServiceemailCreateTest@sip.es");
                this.userBuilderUpdate.setUsername("adminServiceusernameRoles");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByDni("adminServicedniCreateTest", userBuilderUpdate),
                                "The Username already exists: adminServiceusernameRoles");

                userRepository.delete(userBuilderCreate);
                userRepository.delete(userBuilder);

        }

        @Test
        void shouldUpdateByUsername() {

                createUser();

                assertThrows(ResourceNotFoundException.class,
                                () -> adminService.updateByUsername("UsernameNotExist", userBuilderCreate),
                                "The username don't exist: UsernameNotExist");
                assertDoesNotThrow(() -> adminService.updateByUsername("adminServiceemailCreateTest@sip.es",
                                userBuilderCreate));
                assertEquals("adminServiceemailCreateTest@sip.es", userBuilderCreate.getUsername());
                assertEquals("adminServiceemailCreateTest@sip.es", userBuilderUpdate.getUsername());
                assertEquals("adminServicephoneCreateTest", userBuilderUpdate.getPhone());

                this.userBuilderUpdate.setPhone("adminServicephoneTest");
                assertThrows(ResourceConflictException.class, () -> adminService
                                .updateByUsername("adminServiceemailCreateTest@sip.es", userBuilderUpdate),
                                "The mobile already exists: adminServicephoneTest");

                this.userBuilderUpdate.setPhone("adminServicephoneCreateTest");
                this.userBuilderUpdate.setDni("adminServicedniTest");
                assertThrows(ResourceConflictException.class, () -> adminService
                                .updateByUsername("adminServiceemailCreateTest@sip.es", userBuilderUpdate),
                                "The dni already exists: adminServicedniTest");

                this.userBuilderUpdate.setDni("adminServicedniCreateTest");
                this.userBuilderUpdate.setEmail("adminServiceemailTest@sip.es");
                assertThrows(ResourceConflictException.class,
                                () -> adminService.updateByUsername("adminServiceemailCreateTest@sip.es",
                                                userBuilderUpdate),
                                "The email already exists: adminServiceemailTest@sip.es");

                userRepository.delete(userBuilderCreate);
                userRepository.delete(userBuilder);

        }

}
