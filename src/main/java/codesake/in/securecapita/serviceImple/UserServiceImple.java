package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.DTOMapper.UserDTOMapper;
import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Role;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.dto.UserRolesDto;
import codesake.in.securecapita.repos.RoleRepos;
import codesake.in.securecapita.repos.UserRepos;
import codesake.in.securecapita.service.JwtServices;
import codesake.in.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImple implements UserService {

    private final UserRepos<User> userRepos;

    private final RoleRepos<Role> roleRepos;

    @Autowired
    private JwtServices jwtServices;




    @Override
    public UserDTO createUser(User user) throws CatchGlobalException {
        return UserDTOMapper.fromUser(userRepos.create(user));
    }

    @Override
    public User loadUserByEmail(String username) throws UsernameNotFoundException {

        User user=userRepos.findUserByEmail(username);
        user.setRole(GetUserRolesById(user.getId()).get(0).getRoleName());
        return user;
    }

    @Override
    public List<UserRolesDto> GetUserRolesById(Long id) {
        return  roleRepos.getUserRolesById(id);
    }


}
