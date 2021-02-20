package es.sipinformatica.propertymanagement.security.api.httperrors;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    public static ResponseEntity<Object> build(ApiErrorMessage apiErrorMessage){
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }
    
}
