package codesake.in.securecapita.service;

import codesake.in.securecapita.dto.UserDTO;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtServices {
    public String extractUsername(String token);
    public Date extractExpiration (String token);

    public Boolean isTokenExpired(String token);

    public Boolean validateToken(String token, UserDetails userDetails);

    public  <T> T extractClaims(String token, Function<Claims,T> clamsResolver);

    public String GenerateToken(UserDTO userDTO);

}
