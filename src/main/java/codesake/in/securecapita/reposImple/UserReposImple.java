package codesake.in.securecapita.reposImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.Role;
import codesake.in.securecapita.domain.User;

import codesake.in.securecapita.repos.RoleRepos;
import codesake.in.securecapita.repos.UserRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static codesake.in.securecapita.enumeration.RoleTypeEnum.ROLE_USER;
import static codesake.in.securecapita.enumeration.VerificationTypeEnum.ACCOUNT;
import static codesake.in.securecapita.sqlQuery.UserSqlQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserReposImple implements UserRepos<User> {


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepos<Role> roleRepos;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User create(User user) throws CatchGlobalException {

        //Check the email is unique

        if(getEmailCount(user.getEmail().trim().toLowerCase())>0) throw new CatchGlobalException("Email already in use, Please use different email.",HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());

        try{
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameterSource = getSqlParameterSource(user);

            //save new user
            jdbcTemplate.update(INSERT_USER_QUERY,parameterSource,holder);
            user.setId(requireNonNull(holder.getKey()).longValue());

            //Add role to user
            roleRepos.addRoleToUser(user.getId(),ROLE_USER.name());


            //send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());

            //save url in verification table
            jdbcTemplate.update(INSERT_VERIFICATION_QUERY,Map.of("userId",user.getId(),"url",verificationUrl));



            //send email to user with verification url
            //emailService.sendEmailVerificationUrlOrPassword(user.getFirstName(),user.getEmail(),verificationUrl,ACCOUNT.getType());
            user.setEnabled(0);
            user.setIsNonLocked(0);
            // return the newly created user
            return user;
            // if any errors, throw exception with proper message

        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }




    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, Map.of("email", email), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return User.builder()
                        .enabled(rs.getInt("enabled"))
                        .isNonLocked(rs.getInt("non_locked"))
                        .isUsingMfa(rs.getInt("using_mfa"))
                        .bio(rs.getString("bio"))
                        .email(rs.getString("email"))
                        .id(rs.getLong("id"))
                        .password(rs.getString("password"))
                        .build();
            }
        });
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {

        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email",email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {

        return new MapSqlParameterSource()
                .addValue("firstName",user.getFirstName())
                .addValue("lastName",user.getLastName())
                .addValue("email",user.getEmail())
                .addValue("password",passwordEncoder.encode(user.getPassword()))
                .addValue("enabled",0);



    }

    private String getVerificationUrl(String key, String type ){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify"+type+"/"+key).toUriString();

    }




}
