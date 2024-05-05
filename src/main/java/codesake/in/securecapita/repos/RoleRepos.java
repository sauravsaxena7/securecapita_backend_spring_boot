package codesake.in.securecapita.repos;

import codesake.in.securecapita.domain.Role;
import codesake.in.securecapita.domain.User;

import java.util.Collection;

public interface RoleRepos<T extends Role> {

        /*basic crud operation*/

        T create(T data);
        Collection<T> list(int page, int pageSize);

        T get(Long id);
        T update(T data);

        Boolean delete(Long id);

        void addRoleToUser(Long userId, String roleName);

        Role getRoleByUserId(Long userId);

        Role getRoleByUserEmail(String email);

        void updateUserRole(Long userId, String roleName);
}
