package codesake.in.securecapita.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDto {
    Long id;
    Long userId;
    Long roleId;
    String roleName;
    String permissions;
}
