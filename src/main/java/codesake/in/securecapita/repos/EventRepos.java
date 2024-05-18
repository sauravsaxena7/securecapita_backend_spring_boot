package codesake.in.securecapita.repos;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Event;
import codesake.in.securecapita.dto.UserEventsDTO;
import java.util.Collection;
import java.util.List;

public interface EventRepos<T extends Event>{
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);
    void addEventsActivityToUser(Long userId, String event_name,String device,String ip_address) throws CatchGlobalException;
    List<UserEventsDTO> getAllEventsActivityByUserId(Long userId) throws CatchGlobalException;
}
