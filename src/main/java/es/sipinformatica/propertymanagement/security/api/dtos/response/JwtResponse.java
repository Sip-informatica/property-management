package es.sipinformatica.propertymanagement.security.api.dtos.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JwtResponse {

    private String token;  
    private String username;
    private String email;
    private String phone;
    private String dni;
    private List<String> roles;
}
