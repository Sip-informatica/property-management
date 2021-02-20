package es.sipinformatica.propertymanagement.security.configurations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import es.sipinformatica.propertymanagement.security.api.httpserrors.ApiErrorMessage;
import es.sipinformatica.propertymanagement.security.api.httpserrors.ResponseEntityBuilder;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error; {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 
        List<String> detailsError = new ArrayList<>();
        detailsError.add(authException.getMessage());
        detailsError.add("You need to login first in order to perform this action.");
        ApiErrorMessage error = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED, 
            "Unauthorized Exception - 401 ",
            detailsError);
        
        final ResponseEntity<?> responseEntity =  ResponseEntityBuilder.build(error);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseEntity); 
        
    }    

}
