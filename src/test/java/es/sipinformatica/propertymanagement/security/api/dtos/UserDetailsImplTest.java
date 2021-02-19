package es.sipinformatica.propertymanagement.security.api.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;


public class UserDetailsImplTest {
    private final User user = new User(); 
    Set<Role> roles = new HashSet<>();
    Role roleAdmin = new Role();
    Role roleManager = new Role();
    Role roleCustomer = new Role(); 
    

    @BeforeEach
	public void initData() {      
       
        roleAdmin.setName(ERole.ROLE_ADMIN);    
        roleManager.setName(ERole.ROLE_MANAGER);    
        roles.add(roleAdmin);
        roles.add(roleManager);
        user.setId(1L);       
        user.setRoles(roles);
        user.setUsername("AdminManager");
        user.setEmail("adminmanager@sip.es");
        user.setPassword("passAdminManager");
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(true);

    }
    
    @Test
    public void  rolesOfUser() {
                 
        assertNotNull(user);         
        assertTrue(user.getRoles().contains(roleAdmin));
        assertTrue(user.getRoles().contains(roleManager));
        assertFalse(user.getRoles().contains(roleCustomer));        

    }
    @Test
	public void buildUserDetailsShouldConvertFromUser() {
        List<String> expectedAuthorities = Arrays.asList("ROLE_ADMIN", "ROLE_MANAGER");
        UserDetails userDetails = UserDetailsImpl.build(user);
        final List<String> userAuthorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), user.getUsername());
        assertEquals(userDetails.getPassword(), user.getPassword());
        assertTrue(userDetails.getAuthorities().stream().findAny().isPresent());
        assertTrue(userAuthorities.containsAll(expectedAuthorities));

        

    }
}
