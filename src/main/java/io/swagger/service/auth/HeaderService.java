package io.swagger.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.security.jwt.JwtConfig;
import io.swagger.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class HeaderService {

    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final HttpServletRequest request;


    /**
     * @return the username that is stored in Authorization Header
     * @throws io.jsonwebtoken.JwtException if bearer token is not trusted
     */
    public String getUsername() {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (authorizationHeader == null || authorizationHeader.isEmpty())
            return null;

        String token = authorizationHeader.substring(7);
        Jws<Claims> claimsJws = jwtService.claimsJws(token);
        Claims body = claimsJws.getBody();
        return body.getSubject();
    }
}
