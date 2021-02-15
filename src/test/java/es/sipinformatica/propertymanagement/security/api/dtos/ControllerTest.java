package es.sipinformatica.propertymanagement.security.api.dtos;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTest {
    public static final String BASEURL = "http://localhost:8080/api/test/";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "MANAGER")
    public void allAccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, BASEURL + "all"))        
        .andExpect(status().isOk())
        .andExpect(content().string("Public Content."));
              
    }

    @Test
    @WithMockUser(username = "manager", password = "pass", roles = {"MANAGER" , "ADMIN"})
    public void adminmanager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, BASEURL + "adminmanager"))        
        .andExpect(status().isOk())
        .andExpect(content().string("AdminManager Board."));
              
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, BASEURL + "manager"))        
        .andExpect(status().isForbidden());              
    }
    
}
