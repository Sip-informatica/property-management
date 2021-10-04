package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import es.sipinformatica.propertymanagement.security.api.dtos.UserDto;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminResourceIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;

    private UserDto userBuilderCreate;
    private UserDto userBuilder;
    Set<String> roles = new HashSet<>();

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void userInit() {
        this.roles.add("ROLE_ADMIN");
        this.roles.add("ROLE_CUSTOMER");

        this.userBuilderCreate = UserDto.builder().email("emailCreateTest@sip.es").username("Create").phone("639736746")
                .dni("dniCreateTest").password("password").build();
        this.userBuilder = UserDto.builder().username("usernameRoles").email("emailTest@sip.es").phone("phoneTest")
                .dni("dniTest").password("password").roles(roles).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readAllTest() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, this.restTemplate.getRootUri() + "api/auth/users-admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void readAllForbiddenTest() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, this.restTemplate.getRootUri() + "api/auth/users-admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readUserByEmailTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
                        this.restTemplate.getRootUri() + "api/auth/users-admin/email/q@q.es"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.errors").value("The email don't exist: q@q.es"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreate() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(this.restTemplate.getRootUri() + "api/auth/users-admin")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userBuilder)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.message").hasJsonPath());

        User userTest = userRepository.findByEmail(userBuilder.getEmail()).orElseThrow();
        userRepository.delete(userTest);
    }

}
