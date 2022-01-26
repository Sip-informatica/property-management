package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sipinformatica.propertymanagement.security.api.dtos.request.UserSignupRequest;

@RestController
@RequestMapping("/api/auth")
public class AcocountResource {

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        return null;

    }
}
