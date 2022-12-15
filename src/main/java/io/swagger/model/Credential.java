package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.security.auth.ApplicationUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * User login data class
 */
@Schema(description = "user login data")
@Validated
@Entity
@Table
@Data
public class Credential implements UserDetails {

    public Credential(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = ApplicationUserRole.valueOf(role);

        this.accountNonLocked = false;
        this.enabled = false;
    }

    public Credential() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private ApplicationUserRole role;

    @JsonIgnore
    private boolean accountNonLocked;

    @JsonIgnore
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The email of a username and password credential pair.
     *
     * @return email
     **/
    @Schema(example = "exmpl@example.com", required = true, description = "The email of a username and password credential pair.")
    @NotNull
    public String getUsername() {
        return username;
    }

    /**
     * The password of a username and password credential pair.
     *
     * @return password
     **/
    @Schema(example = "password123", required = true, description = "The password of a username and password credential pair.")
    @NotNull
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Bean
    @JsonIgnore
    public Collection getAuthorities() {
        return role.getGrantedAuthority();
    }

    @Schema(description = "User's role")
    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateAccount(Credential body) {
        this.username = body.username;
        this.password = body.password;
    }
}
