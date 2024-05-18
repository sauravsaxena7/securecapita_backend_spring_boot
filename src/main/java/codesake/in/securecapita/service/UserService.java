package codesake.in.securecapita.service;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.dto.UserEventsDTO;
import codesake.in.securecapita.dto.UserRolesDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService  {
    UserDTO createUser(User user) throws CatchGlobalException;

    User loadUserByEmail(String username) throws UsernameNotFoundException, CatchGlobalException;

    List<UserRolesDto> GetUserRolesById(Long id) throws CatchGlobalException;

    List<UserEventsDTO> GetUserEventsActivityById(Long id) throws CatchGlobalException;

    void AddEventsActivityToUser (Long userId, String event_name, String device, String ip_address) throws CatchGlobalException;


    boolean verifyUserTokenForActivatingUser(String token);
}
