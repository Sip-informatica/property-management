package es.sipinformatica.propertymanagement.security.api.httpserrors;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    private ResponseEntityBuilder(){
        throw new IllegalStateException("Response Entity Builder");
    }

    public static ResponseEntity<Object> build(ApiErrorMessage apiErrorMessage){
        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }
    
}
