package es.sipinformatica.propertymanagement.security.api.resources;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ExceptionResourceIT {
    public static final String EXCEPTIONS = "api/test/exception";
	public static final String ID_ID = "/{id}";
    public static final String API_TEST = "/api/test";
    public static final String API = "/api/auth";

    @Autowired
    private WebTestClient webTestClient;   
   
    @Test
    void testForbiddenException(){
        this.webTestClient
        .get().uri(API_TEST + "/admin")
        .exchange()
        .expectStatus()
        .isForbidden();        
    }
    @Test
    void testUnauthorizedException(){
        this.webTestClient
        .post().uri("")
        .exchange()
        .expectStatus()
        .isUnauthorized();        
    }
    @Test
    void testException(){
        this.webTestClient
        .get().uri(EXCEPTIONS + "/handleall")
        .exchange()
        .expectStatus()
        .is5xxServerError();   
    }
       
}
