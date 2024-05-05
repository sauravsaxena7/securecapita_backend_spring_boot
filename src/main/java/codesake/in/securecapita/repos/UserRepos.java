package codesake.in.securecapita.repos;

import codesake.in.securecapita.domain.User;

import java.util.Collection;

public interface UserRepos<T extends User> {

    /*basic crud operation*/

    T create(T data);
    Collection<T> list(int page,int pageSize);

    T get(Long id);
    T update(T data);

    Boolean delete(Long id);

    //More complex operations



}
