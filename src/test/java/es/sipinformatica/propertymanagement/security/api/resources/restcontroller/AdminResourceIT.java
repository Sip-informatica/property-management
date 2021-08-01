package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminResourceIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser(roles = "ADMIN")
    void readAllTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/auth/users-admin"))        
        .andExpect(status().isOk());                      
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void readAllForbiddenTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/auth/users-admin"))        
        .andExpect(status().isForbidden());                      
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readUserByEmailTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/auth/users-admin/email/q@q.es"))        
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.errors").value("The email don't exist: q@q.es"))
        .andExpect(jsonPath("$.message").isNotEmpty())
        ;                         
    }
}
