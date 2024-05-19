package codesake.in.securecapita.serviceImple;

import codesake.in.securecapita.dto.UserDTO;
import codesake.in.securecapita.service.JwtServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtServicesImple implements JwtServices {
    public static final String SECRET = "HDUWYR83R9FHQLVLWVNOHVU9WUF92HPGNVKJWPOOPEWVOU";

    @Override
    public String extractUsername(String token) {
        return extractClaims(token,claims -> claims.getSubject());
    }

    public Map<String, Object> extractTokenVerificationClaims(String token) {
        Map<String, Object> claims = extractAllClaims(token);
        return claims;
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaims(token,claims -> claims.getExpiration());
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private String createToken(Map<String,Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*100*60*60*10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    private String CreateVerificationToken(Map<String,Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> clamsResolver) {

        final Claims claims = extractAllClaims(token);
        return clamsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);;
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String GenerateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDTO.getRole());

        return createToken(claims, userDTO.getEmail());
    }
    @Override
    public String GenerateVerificationToken(String username, String type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type",type);

        return CreateVerificationToken(claims, username);
    }
}
