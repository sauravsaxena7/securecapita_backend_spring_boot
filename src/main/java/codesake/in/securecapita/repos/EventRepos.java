package codesake.in.securecapita.repos;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Event;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EventRepos<T extends Event>{
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);
    void addEventsActivityToUser(Long userId, String event_name,String device,String ip_address) throws CatchGlobalException;
    List<Map<String, Object>> getAllEventsActivityByUserId(Long userId) throws CatchGlobalException;
}
