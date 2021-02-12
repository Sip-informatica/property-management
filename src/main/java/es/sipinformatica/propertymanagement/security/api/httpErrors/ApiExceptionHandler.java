package es.sipinformatica.propertymanagement.security.api.httpErrors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.sipinformatica.propertymanagement.security.domain.exceptions.BadRequestException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ConflictException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.ForbiddenException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.NotFoundException;
import es.sipinformatica.propertymanagement.security.domain.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
        AccessDeniedException.class,
        UnauthorizedException.class})
    @ResponseBody
    public void unauthorizedRequest(Exception exception){
        log.debug("Unauthorized: " + exception.getMessage());
        
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorMessage notFoundExceptionRequest(Exception exception){
        return new ErrorMessage(exception, HttpStatus.NOT_FOUND.value());

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadRequestException.class,
            DuplicateKeyException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseBody
    public ErrorMessage badRequest(Exception exception) {
        return new ErrorMessage(exception, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    @ResponseBody
    public ErrorMessage conflict(Exception exception) {
        return new ErrorMessage(exception, HttpStatus.CONFLICT.value());
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ErrorMessage forbidden(Exception exception) {
        return new ErrorMessage(exception, HttpStatus.FORBIDDEN.value());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorMessage exception(Exception exception) { 
        exception.printStackTrace(); 
        return new ErrorMessage(exception, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    
}
