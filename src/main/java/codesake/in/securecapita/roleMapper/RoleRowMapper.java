package codesake.in.securecapita.roleMapper;

import codesake.in.securecapita.domain.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.builder()
                .id(resultSet.getLong("id"))
                .permissions(resultSet.getString("name"))
                .name(resultSet.getString("name")).build();
    }
}
