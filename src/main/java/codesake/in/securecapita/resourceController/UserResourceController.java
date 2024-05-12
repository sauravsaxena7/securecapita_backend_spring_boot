package codesake.in.securecapita.resourceController;


import codesake.in.securecapita.ApiResponse.HttpApiResponse;
import codesake.in.securecapita.DTOMapper.UserDTOMapper;
import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.dto.UserRolesDto;
import codesake.in.securecapita.service.JwtServices;
import codesake.in.securecapita.service.UserService;
import codesake.in.securecapita.serviceImple.CustomerUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserResourceController {
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServices jwtServices;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;


    @PostMapping("/signUp")
    public ResponseEntity<HttpApiResponse> SignUpUser(@RequestBody User user) throws CatchGlobalException {
        UserDTO userDTO = userService.createUser(user);
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


    @PostMapping("/login")
    public ResponseEntity<HttpApiResponse> Login(@RequestBody User user) throws CatchGlobalException {
        log.info("Inside login controller");
        try{
            log.info("Inside try catch block try to print isAutnticated");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

            log.info("Inside try catch block of login controller");
            if(authentication.isAuthenticated()){
                UserDTO userDTO = customerUserDetailsService.getUserDTO();
                if(!customerUserDetailsService.isEnabled()) throw new CatchGlobalException("Failed to loign! Account Is Not Active, Please check your Inbox verify it.", HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
                userDTO.setToken(jwtServices.GenerateToken(userDTO));
               HttpApiResponse response = HttpApiResponse.builder()
                       .timeStamp(LocalDateTime.now().toString())
                       .data(Map.of("user",userDTO))
                       .message("Login Successful.")
                       .success(true)
                       .statusCode(HttpStatus.OK.value())
                       .build();

                return new ResponseEntity<>(response, HttpStatus.OK);

            }


            throw new CatchGlobalException("Bad Credential.",HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }






    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/getUserRolesById/{id}")
    public ResponseEntity<HttpApiResponse> GetUserRolesById(@PathVariable Long id){
        List<UserRolesDto> roles = userService.GetUserRolesById(id);
        return ResponseEntity.created(getUri()).body(
                HttpApiResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("UserRoles",roles))
                        .message("UserRoles Fetched")
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }



    private URI getUri(){
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }








}
