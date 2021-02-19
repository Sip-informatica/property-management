package es.sipinformatica.propertymanagement.security.api.dtos.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
    
}
