package es.sipinformatica.propertymanagement.security.domain.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceForbiddenExceptionTest {
    private ResourceForbiddenException resourceForbiddenException;

    @BeforeEach
    void ResourceForbiddenExceptionInit(){
        resourceForbiddenException =
        new ResourceForbiddenException("message ResourceForbiddenException ");        
    }
    @Test
    void shouldGetMessage(){
        assertTrue(resourceForbiddenException.getMessage()
        .contentEquals("message ResourceForbiddenException "));
    }   
}