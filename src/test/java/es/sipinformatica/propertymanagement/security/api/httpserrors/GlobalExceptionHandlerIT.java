package es.sipinformatica.propertymanagement.security.api.httpserrors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
class GlobalExceptionHandlerIT {
    private static final String API = "/api/auth";
    private static final String API_TEST = "/api/test/";
    private static final String SIGNIN = "/signin";
   
    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldHttpstatusBadRequest(){
        this.webTestClient.post()
        .uri(API + SIGNIN)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();        
    }
    @Test
    void shouldHttpstatusMethodNotSupported(){
        this.webTestClient.get()
        .uri(API + SIGNIN)        
        .exchange()
        .expectStatus()
        .isBadRequest();        
    }
    @Test
    void shouldHttpstatusUnauthorized(){
        this.webTestClient.get()
        .uri("")        
        .exchange()
        .expectStatus()
        .isUnauthorized();        
    }
    @Test
    void shouldHttpstatusMethodNotFound(){
        this.webTestClient.get()
        .uri(API)        
        .exchange()
        .expectStatus()
        .isBadRequest();        
    }
    @Test
    void shouldHttpstatusForbidden(){
        this.webTestClient.get()
        .uri(API_TEST + "admin")        
        .exchange()
        .expectStatus()
        .isForbidden();        
    }
    @Test
    void shouldHttpstatusInternalServerError(){
        this.webTestClient.get()
        .uri(API_TEST + "exception/handleall")        
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody().jsonPath("message", "Error occurred");        
    }
       
}
