package es.sipinformatica.propertymanagement.security.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceBadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ResourceBadRequestException(String message) {
        super(message);
    }
    
}

