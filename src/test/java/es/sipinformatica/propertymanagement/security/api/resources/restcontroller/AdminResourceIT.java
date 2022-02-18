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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import es.sipinformatica.propertymanagement.security.api.dtos.UserDto;

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

        @BeforeEach
        void userInit() {
                this.roles.add("ROLE_ADMIN");
                this.roles.add("ROLE_CUSTOMER");

                this.userBuilderCreate = UserDto.builder().username("Create").email("emailTest@sip.es")
                                .phone("635736766")
                                .dni("25524994P").password("2Ppassword").roles(roles).isEnabled(true).build();
                this.userBuilder = UserDto.builder().username("usernameRoles").email("emailTest@sip.es")
                                .phone("145784578")
                                .dni("37721298X").password("3Ppassword").roles(roles).build();
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
                this.mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,
                                this.restTemplate.getRootUri() + "/api/admin/users-admin")).andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        void readAllForbiddenTest() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET,
                                this.restTemplate.getRootUri() + "/api/admin/users-admin"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void readUserByEmailTest() throws Exception {
                this.mockMvc
                                .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
                                                this.restTemplate.getRootUri() + "/api/admin/users-admin/email/q@q.es"))
                                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value("NOT_FOUND"))
                                .andExpect(jsonPath("$.errors").value("The email don't exist: q@q.es"))
                                .andExpect(jsonPath("$.message").isNotEmpty());
        }

        @Transactional
        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldCreate() throws Exception {

                this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .post(this.restTemplate.getRootUri() + "/api/admin/users-admin")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(asJsonString(userBuilder)))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.message").hasJsonPath());

        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldReadUpdateDeleteByEmail() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders
                                .post(this.restTemplate.getRootUri() + "/api/admin/users-admin")
                                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userBuilder)));

                this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .get(this.restTemplate.getRootUri() + "/api/admin/users-admin/email/"
                                                                + userBuilder.getEmail()))
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("emailTest@sip.es"));

                this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .put(this.restTemplate.getRootUri() + "/api/admin/users-admin/email/"
                                                                + userBuilder.getEmail())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(asJsonString(userBuilderCreate)))
                                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("emailTest@sip.es - User updated successfully"));

                userBuilderCreate.setUsername("manager");

                this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .put(this.restTemplate.getRootUri() + "/api/admin/users-admin/email/"
                                                                + userBuilder.getEmail())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(asJsonString(userBuilderCreate)))
                                .andExpect(status().isConflict()).andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                .value("The Username already exists: manager"));

                this.mockMvc
                                .perform(MockMvcRequestBuilders.delete(
                                                this.restTemplate.getRootUri() + "/api/admin/users-admin/email/"
                                                                + userBuilder.getEmail()))
                                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("emailTest@sip.es - User deleted successfully"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldDeleteByDni() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders
                                .post(this.restTemplate.getRootUri() + "/api/admin/users-admin")
                                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userBuilder)));

                this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .delete(this.restTemplate.getRootUri() + "/api/admin/users-admin/dni/"
                                                                + userBuilder.getDni()))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldDeleteByUsername() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders
                                .post(this.restTemplate.getRootUri() + "/api/admin/users-admin")
                                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userBuilder)));

                this.mockMvc
                                .perform(MockMvcRequestBuilders.delete(
                                                this.restTemplate.getRootUri() + "/api/admin/users-admin/username/"
                                                                + userBuilder.getUsername()))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void shouldDeleteByPhone() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders
                                .post(this.restTemplate.getRootUri() + "/api/admin/users-admin")
                                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userBuilder)));

                this.mockMvc
                                .perform(MockMvcRequestBuilders.delete(
                                                this.restTemplate.getRootUri() + "/api/admin/users-admin/phone/"
                                                                + userBuilder.getPhone()))
                                .andExpect(status().isOk());
        }

}
