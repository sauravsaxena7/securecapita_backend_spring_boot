package codesake.in.securecapita.resourceController;


import codesake.in.securecapita.ApiResponse.HttpApiResponse;
import codesake.in.securecapita.DTOMapper.UserDTOMapper;
import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.domain.VerificationDomain;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.dto.UserEventsDTO;
import codesake.in.securecapita.dto.UserRolesDto;
import codesake.in.securecapita.repos.VerificationRepos;
import codesake.in.securecapita.service.EmailService;
import codesake.in.securecapita.service.JwtServices;
import codesake.in.securecapita.service.UserService;
import codesake.in.securecapita.serviceImple.CustomerUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static codesake.in.securecapita.enumeration.EventTypeEnum.LOGIN_ATTEMPT_FAILURE;
import static codesake.in.securecapita.utils.Utils.getHost;

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

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final VerificationRepos<VerificationDomain> verificationRepos;

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

        try{
            //emailService.sendSimpleEmailMessage("SAURAV SAXENA","sauravsaxena121@gmail.com","");
            //emailService.sendMimeEmailMessageWithAttachments("Saurav saxena ","sauravsaxena121@gmail.com","");
            //emailService.sendHtmlEmail("saurav","sauravsaxena121@gmail.com","");
           emailService.sendHtmlEmailEmbedFiles("saurav", user.getEmail(), "");
            log.info("Inside try catch block try to print isAutnticated");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
            if(authentication.isAuthenticated()){
                UserDTO userDTO = customerUserDetailsService.getUserDTO();
                if(!customerUserDetailsService.isEnabled()) throw new CatchGlobalException("Failed to loign! Account Is Not Active, Please check your email and verify it.", HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
                userDTO.setToken(jwtServices.GenerateToken(userDTO));
                HttpApiResponse response = HttpApiResponse.getSuccessHttpApiResponse("Login Successful.",Map.of("user",userDTO),HttpStatus.OK.value());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            throw new CatchGlobalException("Invalid Credential. ",HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
        }catch (BadCredentialsException ex){
            UserEventsDTO userEventsDTO = getHost(httpServletRequest);
            log.info("Inside block of login controller customeruserdetailsservices: {}",customerUserDetailsService);
            log.info("Inside login controller httpservletrequest: {}",userEventsDTO);
            //userService.AddEventsActivityToUser(customerUserDetailsService.getUserDTO().getId(), LOGIN_ATTEMPT_FAILURE.name(),userEventsDTO.getDevice(),userEventsDTO.getIp_address());
            throw new CatchGlobalException(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/getUserRolesById/{id}")
    public ResponseEntity<HttpApiResponse> GetUserRolesById(@PathVariable Long id) throws CatchGlobalException {
        List<UserRolesDto> roles = userService.GetUserRolesById(id);
        HttpApiResponse response = HttpApiResponse.getSuccessHttpApiResponse( !roles.isEmpty()?"User Roles Fetched":"Empty Result Set.",Map.of("role",roles),HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/getUserEventsActivityById/{id}")
    public ResponseEntity<HttpApiResponse> GetUserEventsActivityById(@PathVariable Long id) throws CatchGlobalException {
        List<Map<String, Object>> list = userService.GetUserEventsActivityById(id);
        HttpApiResponse response = HttpApiResponse.getSuccessHttpApiResponse(!list.isEmpty() ?"User Activity Fetched":"Empty Result Set.",Map.of("activity",list),HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping(path = "/confirmUserAccount")
    public ResponseEntity<HttpApiResponse> ConfirmOrActivateUser(@RequestParam("token") String token) throws CatchGlobalException {
        //boolean isSuccess = userService.verifyUserTokenForActivatingUser(token);

        Boolean isAccountVerified = verificationRepos.isAccountEnabled(token);

        HttpApiResponse response = HttpApiResponse.getSuccessHttpApiResponse(
                isAccountVerified.equals(Boolean.TRUE)
                        ? "Your Account Is now Activated."
                        :
                        "Account Verification Failed."
         ,null,HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private URI getUri(){
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }


}
