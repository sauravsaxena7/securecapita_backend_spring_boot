package codesake.in.securecapita.service;

import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserDTO;

public interface UserService {
    UserDTO createUser(User user);
}
