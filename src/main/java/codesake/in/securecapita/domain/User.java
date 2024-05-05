package codesake.in.securecapita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class User {

    private Long id;
    @NotEmpty(message = "First name required")
    private String firstName;

    @NotEmpty(message = "Last name required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid Email.")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;


    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNonLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;





}
