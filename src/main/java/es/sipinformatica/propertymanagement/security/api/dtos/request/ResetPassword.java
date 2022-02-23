package es.sipinformatica.propertymanagement.security.api.dtos.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResetPassword {
     // digit + lowercase char + uppercase char
     private static final String P_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
     @NonNull
     @NotBlank
     @Size(min = 6, max = 40)
     @Pattern(regexp = P_PATTERN, message = "Password must be at least 8 characters and contain at least one digit, one lowercase and one uppercase letter")
     private String newPassword;
     private String resetToken;    
    
}
