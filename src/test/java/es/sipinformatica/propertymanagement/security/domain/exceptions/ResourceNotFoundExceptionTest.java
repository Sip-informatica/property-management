package es.sipinformatica.propertymanagement.security.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {
    private ResourceNotFoundException resourceNotFoundException;

    @BeforeEach
    void ResourceNotFoundExceptionInit(){
        resourceNotFoundException =
        new ResourceNotFoundException("message ResourceNotFoundException ");        
    }
    @Test
    void shouldGetMessage(){
        assertTrue(resourceNotFoundException.getMessage()
        .contentEquals("message ResourceNotFoundException "));
    }   
}