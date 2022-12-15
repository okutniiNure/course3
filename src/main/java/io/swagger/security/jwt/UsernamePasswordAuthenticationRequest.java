package io.swagger.security.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernamePasswordAuthenticationRequest {
    private String username;
    private String password;
}
