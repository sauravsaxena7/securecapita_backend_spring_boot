package codesake.in.securecapita.roleMapper;

import codesake.in.securecapita.dto.UserRolesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRowMapper  implements RowMapper<UserRolesDto> {
    @Override
    public UserRolesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserRolesDto.builder()
                .userId(rs.getLong("user_id"))
                .permissions(rs.getString("permissions"))
                .roleName(rs.getString("name"))
                .roleId(rs.getLong("role_id"))
                .build();
    }
}
