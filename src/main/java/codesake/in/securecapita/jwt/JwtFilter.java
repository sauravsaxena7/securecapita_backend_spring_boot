package codesake.in.securecapita.jwt;

import codesake.in.securecapita.ApiResponse.HttpApiResponse;
import codesake.in.securecapita.GlobalExceptions.CatchGlobalException;
import codesake.in.securecapita.service.JwtServices;
import codesake.in.securecapita.service.UserService;
import codesake.in.securecapita.serviceImple.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
@AllArgsConstructor
@Data
public class JwtFilter extends OncePerRequestFilter {



    @Autowired
    JwtServices jwtServices;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username = jwtServices.extractUsername(token);
        }
            if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
                if (jwtServices.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);



                }
            }
            filterChain.doFilter(request, response);
    }
}
