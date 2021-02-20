package es.sipinformatica.propertymanagement.security.api.dtos;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser(roles = "MANAGER")
    void allAccessTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/test/all"))        
        .andExpect(status().isOk())
        .andExpect(content().string("Public Content."));
              
    }

    @Test
    @WithMockUser(username = "manager", password = "pass", roles = {"MANAGER" , "ADMIN"})
    void adminmanager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/test/adminmanager"))        
        .andExpect(status().isOk())
        .andExpect(content().string("AdminManager Board."));
              
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(
            HttpMethod.GET, 
            this.restTemplate.getRootUri() + "api/test/manager"))        
        .andExpect(status().isForbidden());              
    }
    
}
