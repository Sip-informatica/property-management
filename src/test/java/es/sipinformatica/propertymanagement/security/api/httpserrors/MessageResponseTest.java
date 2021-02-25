package es.sipinformatica.propertymanagement.security.api.httpserrors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

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
    @Test
    void shouldNotResponseEntityBuilder() throws NoSuchMethodException, SecurityException {
        Constructor<ResponseEntityBuilder> responseEntityBuilder
        = ResponseEntityBuilder.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(responseEntityBuilder.getModifiers()));
        assertThrows(IllegalAccessException.class, () -> {responseEntityBuilder.newInstance();}); 
    }
    
}
