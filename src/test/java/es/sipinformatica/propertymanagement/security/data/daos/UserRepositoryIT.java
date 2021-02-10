package es.sipinformatica.propertymanagement.security.data.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void shouldGetUserByUsername() {
        Optional<User> retrieveUserAdminManager = userRepository.findByUsername("adminManager");
        Optional<User> notRetrieveUserAdmin_Manager = userRepository.findByUsername("admin-Manager");
        
        assertNotNull(retrieveUserAdminManager);
        assertEquals("adminmanager@sip.es", retrieveUserAdminManager.get().getEmail());
        assertTrue(notRetrieveUserAdmin_Manager.isEmpty());
    }

    @Test    
    public void shouldSaveUserWithAssociatedRoles(){           
        
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_OPERATOR).get());
        roles.add(roleRepository.findByName(ERole.ROLE_CUSTOMER).get());
        User newUser = new User("operatorCustomer", "operatorCustomer@sip.es", "passOperatorCustomer");
        newUser.setRoles(roles);
        userRepository.save(newUser);

        User retrieveUserOperatorCustomer = userRepository.findByUsername("operatorCustomer").get();       

        assertNotNull(retrieveUserOperatorCustomer);
        assertTrue(retrieveUserOperatorCustomer.getRoles().containsAll(roles));       
        assertTrue(retrieveUserOperatorCustomer.getRoles().stream()
        .map(Role::getName).collect(Collectors.toList()).toString()
        .contains("ROLE_CUSTOMER"));            

    }   
    
}
