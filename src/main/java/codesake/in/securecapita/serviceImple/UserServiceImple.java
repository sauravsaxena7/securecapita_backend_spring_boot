package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.DTOMapper.UserDTOMapper;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.repos.UserRepos;
import codesake.in.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService {

    private final UserRepos<User> userRepos;
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepos.create(user));
    }
}
