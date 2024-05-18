package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.DTOMapper.UserDTOMapper;
import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.dto.UserRolesDto;
import codesake.in.securecapita.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Data
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerUserDetailsService implements UserDetails,UserDetailsService {

    private String username;
    private String password;

    private UserDTO userDTO;

    @Autowired
    private UserService userService;



    Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        try {
            user = userService.loadUserByEmail(username);
        } catch (CatchGlobalException e) {
            throw new UsernameNotFoundException("Invalid User");
        }
        List<GrantedAuthority> auths= new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(user.getRole()));
        this.authorities=auths;
        this.userDTO=UserDTOMapper.fromUser(user);
        if(!Objects.isNull(user)){
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),auths);
        }

        throw new UsernameNotFoundException("You are not associated with us, Please register yourself");


    }
}
