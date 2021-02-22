package es.sipinformatica.propertymanagement.security.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceBadRequestExceptionTest {
    private ResourceBadRequestException resourceBadRequestException;

    @BeforeEach
    void resourceBadRequestExceptionInit(){
        resourceBadRequestException =
        new ResourceBadRequestException("message resourceBadRequestException ");        
    }
    @Test
    void shouldGetMessage(){
        assertTrue(resourceBadRequestException.getMessage()
        .contentEquals("message resourceBadRequestException "));
    }   
}
