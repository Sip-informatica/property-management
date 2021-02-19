package es.sipinformatica.propertymanagement.security.api.dtos.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSignupRequest {
    @NonNull
    @NotBlank
    @Size(min = 3, max = 20)    
    public String username;
    @NotBlank
    @NonNull
    @Size(max = 50)
    @Email
    public String email;
    @NonNull
    @NotBlank
    @Size(min = 6, max = 40)    
    public String password;
	public Set<String> role;	

}
