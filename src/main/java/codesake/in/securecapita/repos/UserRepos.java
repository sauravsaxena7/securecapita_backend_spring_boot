package codesake.in.securecapita.repos;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.User;

import java.util.Collection;

public interface UserRepos<T extends User> {

    /*basic crud operation*/

    T create(T data) throws CatchGlobalException;
    Collection<T> list(int page,int pageSize);

    T get(Long id);
    T update(T data);

    T findUserByEmail(String email);


    Boolean delete(Long id);

    void setAccountVerificationToken(String username,int userId);





    //More complex operations



}
