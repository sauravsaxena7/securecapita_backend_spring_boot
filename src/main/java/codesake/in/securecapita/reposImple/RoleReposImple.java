package codesake.in.securecapita.reposImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Role;
import codesake.in.securecapita.dto.UserRolesDto;

import codesake.in.securecapita.repos.RoleRepos;
import codesake.in.securecapita.roleMapper.RoleRowMapper;
import codesake.in.securecapita.roleMapper.UserRoleRowMapper;
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


import static codesake.in.securecapita.enumeration.RoleTypeEnum.ROLE_USER;
import static codesake.in.securecapita.sqlQuery.RoleSqlQuery.*;
import static java.util.Objects.requireNonNull;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleReposImple implements RoleRepos<Role> {
    private final NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) throws CatchGlobalException {

        log.info("Adding role {} to user Id {}", roleName ,userId);
        try{

            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name",roleName),new RoleRowMapper());

            jdbcTemplate.update(INSERT_ROLE_TO_USER_QUERY,Map.of("userId",userId,"roleId", requireNonNull(role).getId()));


        }catch (EmptyResultDataAccessException ex){

            throw new CatchGlobalException("No Role Found by name: "+ROLE_USER.name(), HttpStatus.NOT_FOUND.toString(),HttpStatus.NOT_FOUND.value());
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public List<UserRolesDto> getUserRolesById(Long userId) {
        //return Collections.singletonList(jdbcTemplate.queryForObject(GET_ALL_ROLES_BY_USER_ID, Map.of("userId", userId), new BeanPropertyRowMapper<>(UserRolesDto.class)));
        return Collections.singletonList(jdbcTemplate.queryForObject(GET_ALL_ROLES_BY_USER_ID, Map.of("userId", userId), new UserRoleRowMapper()));
    }


    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
