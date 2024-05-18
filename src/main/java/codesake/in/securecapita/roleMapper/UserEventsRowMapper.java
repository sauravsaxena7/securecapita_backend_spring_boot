package codesake.in.securecapita.roleMapper;

import codesake.in.securecapita.dto.UserEventsDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEventsRowMapper implements RowMapper<UserEventsDTO> {
    @Override
    public UserEventsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEventsDTO.builder()
                .userId(rs.getLong("user_id"))
                .event_type(rs.getString("type"))
                .event_description(rs.getString("description"))
                .device(rs.getString("device"))
                .ip_address(rs.getString("ip_address"))
                .id(rs.getLong("id"))
                .build();
    }
}
