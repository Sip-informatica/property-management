package es.sipinformatica.propertymanagement.security.data.daos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.sipinformatica.propertymanagement.security.data.model.ERole;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RoleRepositoryIT {
    @Autowired
    RoleRepository roleRepository;

    @Test
    void shouldFindByName(){
        assertTrue(roleRepository.findByName(ERole.ROLE_ADMIN).isPresent());

    }
    
}
