package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import es.sipinformatica.propertymanagement.security.api.dtos.request.LoginRequest;
import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;
import es.sipinformatica.propertymanagement.security.data.daos.UserRepository;
import es.sipinformatica.propertymanagement.security.data.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthenticationControllerIT {
    private static final String API = "/api/auth";
    private static final String SIGNIN = "/signin";
    private static final String SIGNUP = "/signup";
    private LoginRequest loginRequest;
    private UserSignupRequest userSignupRequest;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void userSignupRequestInit() {
        userSignupRequest = UserSignupRequest.builder().username("username").dni("dni").email("email@sip.es")
                .password("password").phone("phone").build();
    }

    @Test
    void shouldGetSigin() {
        loginRequest = LoginRequest.builder().username("admin").password("passAdmin").build();

        this.webTestClient.post().uri(API + SIGNIN).contentType(MediaType.APPLICATION_JSON).bodyValue(loginRequest)
                .exchange().expectStatus().isOk().expectBody().jsonPath("token").exists();
    }

    @Test
    void shouldRegisterUser() {

        this.webTestClient.post().uri(API + SIGNUP).contentType(MediaType.APPLICATION_JSON).bodyValue(userSignupRequest)
                .exchange().expectStatus().isOk().expectBody().jsonPath("message").exists();

        User userTest = userRepository.findByUsername("username").orElseThrow();
        userRepository.delete(userTest);
    }

    @Test
    void shouldRegisterUserConflict() {
        userSignupRequest.setUsername("AdminManager");
        userSignupRequest.setEmail("adminmanager@sip.es");
        userSignupRequest.setPassword("password");

        this.webTestClient.post().uri(API + SIGNUP).contentType(MediaType.APPLICATION_JSON).bodyValue(userSignupRequest)
                .exchange().expectStatus().is4xxClientError().expectBody().jsonPath("message", "Conflict Exception ");
    }

}
