package es.sipinformatica.propertymanagement.security.api.httpserrors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceBadRequestException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceForbiddenException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // Triggers when the JSON is invalid
	@Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
		
		List<String> detailsError = new ArrayList<>();		
        StringBuilder builder = new StringBuilder();

        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));        
        detailsError.add(builder.toString());

        ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(), 
            HttpStatus.BAD_REQUEST, 
            "Invalid JSON", 
            detailsError);
		
		return ResponseEntityBuilder.build(err);
	
	}
	
	// Triggers when the JSON is malformed
	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, 
        HttpHeaders headers, 
        HttpStatus status, 
        WebRequest request) {
        
		List<String> detailsError = new ArrayList<>();        
        String message = ex.getMessage().isEmpty() ? "Malformed JSON request" : ex.getMessage().split(":", 2)[0];
        detailsError.add(message);
        
        ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Malformed JSON request",
            detailsError);
		
		return ResponseEntityBuilder.build(err);
    }
	
	// Triggers when @Valid fails
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
		HttpHeaders headers, 
        HttpStatus status, 
        WebRequest request) {
		
		List<String> detailsError = ex
                .getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + " : " + error.getDefaultMessage())
				.collect(Collectors.toList());
		
		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
			HttpStatus.BAD_REQUEST,
			"Validation Errors" ,
			detailsError);
		
		return ResponseEntityBuilder.build(err);
	}
	
	// Triggers when there are missing parameters
	@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, 
            HttpHeaders headers,
            HttpStatus status, 
            WebRequest request) {
		
		List<String> detailsError = new ArrayList<>();
		detailsError.add(ex.getParameterName() + " parameter is missing");

		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Missing Parameters",
            detailsError);
		
		return ResponseEntityBuilder.build(err);
    }
	
	// Triggers when a parameter's type does not match
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch
    (MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        List<String> detailsError = new ArrayList<>();
		detailsError.add(ex.getMessage());
      
		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Mismatch Type",
            detailsError);
		
		return ResponseEntityBuilder.build(err);
    }
	
	// Triggers when @Validated fails
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(
        Exception ex, 
        WebRequest request) {
		
		List<String> detailsError = new ArrayList<>();
		detailsError.add(ex.getMessage());
		
		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Constraint Violation",
            detailsError);
		
		return ResponseEntityBuilder.build(err);
	}

    // Triggers when there is not resource with the specified ID in BDD
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
        ResourceNotFoundException ex) {

        List<String> detailsError = new ArrayList<>();
        detailsError.add(ex.getMessage());
        ApiErrorMessage error = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND, 
            "Resource Not Found ",
            detailsError);

        return ResponseEntityBuilder.build(error);
    }    

     // Triggers when there is Conflict BDD
     @ExceptionHandler(ResourceConflictException.class)
     public ResponseEntity<Object> handleResourceConflictException(
        ResourceConflictException ex) {
 
         List<String> detailsError = new ArrayList<>();
         detailsError.add(ex.getMessage());
         ApiErrorMessage error = new ApiErrorMessage(
             LocalDateTime.now(),
             HttpStatus.CONFLICT, 
             "Conflict Exception ",
             detailsError);
 
         return ResponseEntityBuilder.build(error);
     }
     
     // Triggers when there is Access Denied    
     @ExceptionHandler({
         AccessDeniedException.class,
         ResourceForbiddenException.class})
     public ResponseEntity<Object> handleResourceAccessDeniedException(
        Exception ex, WebRequest request) {
 
         List<String> detailsError = new ArrayList<>();
         detailsError.add(ex.getMessage());
         detailsError.add("You don't have required role to perform this action.");
         ApiErrorMessage error = new ApiErrorMessage(
             LocalDateTime.now(),
             HttpStatus.FORBIDDEN, 
             "Access Denied error - 403 ",
             detailsError);
 
         return ResponseEntityBuilder.build(error);
         
     } 
      
      // Triggers when there is Bad Request    
      @ExceptionHandler(ResourceBadRequestException.class)
      public ResponseEntity<Object> handleResourceBadRequestException(
        ResourceBadRequestException ex) {
 
         List<String> detailsError = new ArrayList<>();
         detailsError.add(ex.getMessage());
         ApiErrorMessage error = new ApiErrorMessage(
             LocalDateTime.now(),
             HttpStatus.BAD_REQUEST, 
             "Bad Request Exception ",
             detailsError);
 
         return ResponseEntityBuilder.build(error);
     }

    // Triggers when the handler method is invalid
	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, 
            HttpHeaders headers, 
            HttpStatus status, 
            WebRequest request) {

        List<String> detailsError = new ArrayList<>();
		detailsError.add(String.format("Could not find the %s method for URL %s", 
        ex.getHttpMethod(), 
        ex.getRequestURL()));
		
		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Method Not Found",
            detailsError);
		
        return ResponseEntityBuilder.build(err);
        
    }

     // Triggers when the handler method is Not Supported
	@Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException  ex, 
            HttpHeaders headers, 
            HttpStatus status, 
            WebRequest request) {

        List<String> detailsError = new ArrayList<>();
		detailsError.add(String.format("Supported the %s method ", 
        (Object[])ex.getSupportedMethods()
        ));
		
		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Method Not Supported",
            detailsError);
		
        return ResponseEntityBuilder.build(err);
        
    }
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(
        Exception ex, 
        WebRequest request) {
		
		List<String> detailsError = new ArrayList<>();
        detailsError.add("The error must be corrected"); 
		detailsError.add(ex.getLocalizedMessage()); 
        detailsError.add(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());        

		ApiErrorMessage err = new ApiErrorMessage(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Error occurred",
            detailsError);
		
		return ResponseEntityBuilder.build(err);
	
	}
    
}
