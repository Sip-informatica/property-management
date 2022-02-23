package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.ERole;
import es.sipinformatica.propertymanagement.security.data.model.Role;
import es.sipinformatica.propertymanagement.security.data.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@AutoConfigureMockMvc
class AccountResourceTest {

    private static final String API = "/api/auth";
    private static final String CHANGE_PASSWORD = "/change-password";
    private static final String RESET_PASSWORD = "/reset-password";

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User userBuilder;
    Set<Role> roles = new HashSet<>();

    @BeforeEach
    void userInit() {
        roles.add(Role.builder().name(ERole.ROLE_ADMIN).build());
        String passwordEncode = passwordEncoder.encode("1Password");

        userBuilder = User.builder().username("admin").email("emailTest@sip.es")
                .phone("635736766")
                .dni("25524994P").password(passwordEncode).roles(roles).isEnabled(true).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" }, password = "1Password")
    void shouldValidatePassword() {
        webTestClient.post().uri(API + CHANGE_PASSWORD).contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"oldPassword\":\"1Password\",\"newPassword\":\"asswordnew\"}")
                .exchange().expectStatus().isBadRequest().expectBody().jsonPath("message")
                .isEqualTo("Validation Errors");

    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" }, password = "1Password")
    void shouldChangePassword() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(userBuilder));
        this.webTestClient.post().uri(API + CHANGE_PASSWORD).contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"oldPassword\":\"1Password\",\"newPassword\":\"2Passwordnew\"}")
                .exchange().expectStatus().isOk().expectBody().jsonPath("message").isEqualTo("Password changed successfully");

    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" }, password = "1Password")
    void shouldChangePasswordNotCorrect() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(userBuilder));
        this.webTestClient.post().uri(API + CHANGE_PASSWORD).contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"oldPassword\":\"Password\",\"newPassword\":\"2Passwordnew\"}")
                .exchange().expectStatus().isNotFound().expectBody().jsonPath("errors").isEqualTo("Error: Old password is not correct");

    }

}
