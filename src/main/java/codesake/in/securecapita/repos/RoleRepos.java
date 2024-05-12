package codesake.in.securecapita.repos;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Role;
import codesake.in.securecapita.domain.User;
import codesake.in.securecapita.dto.UserRolesDto;

import java.util.Collection;
import java.util.List;

public interface RoleRepos<T extends Role> {

        /*basic crud operation*/

        T create(T data);
        Collection<T> list(int page, int pageSize);

        T get(Long id);
        T update(T data);

        Boolean delete(Long id);

        void addRoleToUser(Long userId, String roleName) throws CatchGlobalException;

        Role getRoleByUserId(Long userId);

        Role getRoleByUserEmail(String email);
        List<UserRolesDto> getUserRolesById(Long userId);

        void updateUserRole(Long userId, String roleName);
}
