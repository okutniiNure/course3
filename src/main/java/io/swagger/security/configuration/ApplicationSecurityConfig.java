package io.swagger.security.configuration;


import io.swagger.security.auth.CredentialService;
import io.swagger.security.jwt.JwtConfig;
import io.swagger.security.jwt.JwtService;
import io.swagger.security.jwt.JwtTokenVerifier;
import io.swagger.security.jwt.JwtUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.swagger.security.auth.ApplicationUserRole.*;

/**
 * Security configuration class
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final CredentialService credentialService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtService))
                .addFilterAfter(new JwtTokenVerifier(jwtService, jwtConfig), JwtUsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/account/profile").hasAnyRole(USER.name())
                .antMatchers(HttpMethod.PUT, "/account/profile").hasAnyRole(USER.name())
                .antMatchers(HttpMethod.DELETE, "/account/profile").hasAnyRole(USER.name())
                .antMatchers(HttpMethod.GET, "/account/{accountId}").hasRole(USER.name())
                .antMatchers(HttpMethod.PUT, "/account/{accountId}").hasRole(USER.name())
//                .antMatchers( "/mail/**").hasRole(USER.name())
//                .antMatchers( "/mails/**").hasRole(USER.name())
//                .antMatchers( "/group/**").hasRole(USER.name())
                .antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(credentialService);

        return provider;
    }
}
