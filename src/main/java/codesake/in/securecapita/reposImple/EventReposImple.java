package codesake.in.securecapita.reposImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Event;
import codesake.in.securecapita.dto.UserEventsDTO;
import codesake.in.securecapita.repos.EventRepos;
import codesake.in.securecapita.roleMapper.EventRowMapper;
import codesake.in.securecapita.roleMapper.UserEventsRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static codesake.in.securecapita.sqlQuery.EventSqlQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventReposImple implements EventRepos<Event> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Event create(Event data) {
        return null;
    }

    @Override
    public Collection<Event> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Event get(Long id) {
        return null;
    }

    @Override
    public Event update(Event data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addEventsActivityToUser(Long userId, String event_name, String device, String ip_address) throws CatchGlobalException {
        log.info("Adding role {} to user Id {}", event_name ,userId);
        try{
            Event event = jdbcTemplate.queryForObject(SELECT_EVENT_BY_NAME_QUERY, Map.of("type",event_name),new EventRowMapper());
            jdbcTemplate.update(INSERT_EVENT_TO_USER_QUERY,Map.of("userId",userId,"eventId", requireNonNull(event).getId(),"device",device,"ip_address",ip_address));
        }catch (EmptyResultDataAccessException ex){
            throw new CatchGlobalException("No EVENT Found by name: "+event_name, HttpStatus.NOT_FOUND.toString(),HttpStatus.NOT_FOUND.value());
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public List<UserEventsDTO> getAllEventsActivityByUserId(Long userId) throws CatchGlobalException {
        try{
            return Collections.singletonList(jdbcTemplate.queryForObject(GET_ALL_EVENTS_BY_USER_ID, Map.of("userId", userId), new UserEventsRowMapper()));
        }catch (EmptyResultDataAccessException ex){
           return List.of();
        }
        catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
