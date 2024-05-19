package codesake.in.securecapita.reposImple;

import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.domain.VerificationDomain;
import codesake.in.securecapita.repos.VerificationRepos;
import codesake.in.securecapita.service.JwtServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static codesake.in.securecapita.sqlQuery.VerificationSqlQuery.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class VerificationReposImple implements VerificationRepos<VerificationDomain> {

    private final NamedParameterJdbcTemplate jdbcTemplate;


    @Autowired
    private final JwtServices jwtServices;

    @Override
    public VerificationDomain create(VerificationDomain data) {
        return null;
    }

    @Override
    public Collection<VerificationDomain> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public VerificationDomain get(Long id) {
        return null;
    }

    @Override
    public VerificationDomain update(VerificationDomain data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public String addOrUpdateVerificationTokenForUserAccount(String username, Long userId, String type) throws CatchGlobalException {

        String token = jwtServices.GenerateVerificationToken(username,type);
        try{
            if(getAccountVerificationCount(username,type)>0){
                jdbcTemplate.update(UPDATE_ACCOUNT_VERIFICATION_TOKEN_EMAIL_AND_TYPE,
                        Map.of("email",username,"type",type,"token",token));
            }else{
                jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_TOKEN,
                        Map.of("email",username,"type",type,"token",token,"userId",userId));

            }
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return token;
    }

    @Override
    public boolean isAccountEnabled(String token) throws CatchGlobalException {
        try{
            Map<String, Object> claims = jwtServices.extractTokenVerificationClaims(token);
            String username = claims.get("sub").toString();
            String type = claims.get("type").toString();
            Integer userId = getTokenInfoFromAccountVerificationTable(token,username,type);

            //checking for if user is already in active mode.
            if(Objects.equals(jdbcTemplate.queryForObject(SELECT_ENABLE_FROM_USER_BY_USER_ID,
                    Map.of("userId", userId), Integer.class), 1))
                throw new CatchGlobalException("User Account is already in active mode.","",0);

            //Now it's time to activate the user
            jdbcTemplate.update(UPDATE_USER_ENABLE_MODE_BY_USER_ID,Map.of("userId",userId,"flag",1));
            return true;
        }catch (CatchGlobalException ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
        }catch (Exception ex){
            throw new CatchGlobalException(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private Integer getTokenInfoFromAccountVerificationTable(String token ,String username,String type) throws CatchGlobalException {
        try{
            return jdbcTemplate.queryForObject(SELECT_USER_ID_BY_TOKEN_EMAIL_AND_TYPE,Map.of("token",token,"email",username,"type",type),Integer.class);
        }catch (Exception ex){
            throw new CatchGlobalException("Invalid Url or Token: "+ex.getMessage(),HttpStatus.BAD_REQUEST.toString(),HttpStatus.BAD_REQUEST.value());
        }
    }

    private Integer getAccountVerificationCount(String email,String type){
        return jdbcTemplate.queryForObject(COUNT_VERIFICATION_TOKEN_EMAIL_USER_ACCOUNT_QUERY,
                Map.of("email",email,"type",type), Integer.class);
    }
}
