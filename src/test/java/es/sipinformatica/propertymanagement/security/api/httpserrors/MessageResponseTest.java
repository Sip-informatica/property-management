package es.sipinformatica.propertymanagement.security.api.httpserrors;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageResponseTest {
    private MessageResponse message;

    @BeforeEach
    void messageResponseInit(){
        message = new MessageResponse("Message Test");
    }
    @Test 
    void shouldGetMessage(){
        assertTrue(message.getMessage().contentEquals("Message Test"));
    }
    
}
