package codesake.in.securecapita.repos;


import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.VerificationDomain;

import java.util.Collection;

public interface VerificationRepos <T extends VerificationDomain>{

    T create(T data);
    Collection<T> list(int page, int pageSize);

    T get(Long id);
    T update(T data);

    Boolean delete(Long id);

    String addOrUpdateVerificationTokenForUserAccount(String username, Long userId, String type) throws CatchGlobalException;

    boolean isAccountEnabled(String token) throws CatchGlobalException;
}
