package es.sipinformatica.propertymanagement.security.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceConflictExceptionTest {
    private ResourceConflictException resourceConflictException;

    @BeforeEach
    void ResourceConflictExceptionInit(){
        resourceConflictException =
        new ResourceConflictException("message ResourceConflictException ");        
    }
    @Test
    void shouldGetMessage(){
        assertTrue(resourceConflictException.getMessage()
        .contentEquals("message ResourceConflictException "));
    }   
}