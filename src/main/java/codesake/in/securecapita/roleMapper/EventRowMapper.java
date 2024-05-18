package codesake.in.securecapita.roleMapper;

import codesake.in.securecapita.domain.Event;
import codesake.in.securecapita.domain.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .id(resultSet.getLong("id"))
                .event_type(resultSet.getString("type"))
                .event_description(resultSet.getString("description")).build();
    }
}
