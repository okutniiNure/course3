package io.swagger.security.auth.token;

import io.swagger.model.Credential;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Confirmation token class
 */
@Data
@NoArgsConstructor
@Validated
@Entity
@Table
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String token;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "credential_id")
    private Credential credential;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             Credential credential) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.credential = credential;
    }
}
