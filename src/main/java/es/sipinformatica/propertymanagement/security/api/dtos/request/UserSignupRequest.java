package es.sipinformatica.propertymanagement.security.api.dtos.request;

import javax.validation.constraints.Email;
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
public class UserSignupRequest {
    // digit + lowercase char + uppercase char
    private static final String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    @NonNull
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @NonNull
    @Size(max = 50)
    @Email
    private String email;
    @NonNull
    @NotBlank
    @Size(min = 6, max = 40)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
    private String dni;
    @NonNull
    @NotBlank
    private String phone;

}
