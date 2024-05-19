package codesake.in.securecapita.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
@RequiredArgsConstructor
@SuperBuilder
@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private int enabled;
    private int isNonLocked;
    private int isUsingMfa;
    private String role;
    private String token;
    private LocalDateTime createdAt;
}
