package codesake.in.securecapita.resourceController;


import codesake.in.securecapita.ApiResponse.HttpApiResponse;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserResourceController {
    private final UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<HttpApiResponse> SignUpUser(@RequestBody User user){
        UserDTO userDTO=userService.createUser(user);

        return ResponseEntity.created(getUri()).body(
                HttpApiResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user",userDTO))
                        .message("User Created")
                        .success(true)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );

    }

    private URI getUri(){
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }


}
