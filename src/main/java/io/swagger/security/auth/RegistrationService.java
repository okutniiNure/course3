package io.swagger.security.auth;

import io.swagger.model.Credential;
import io.swagger.service.mail.SenderEmailService;
import io.swagger.util.EmailValidator;
import io.swagger.security.auth.token.ConfirmationToken;
import io.swagger.service.auth.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final CredentialService credentialService;
    private final EmailValidator emailValidator;
    private final SenderEmailService senderEmailService;
    private final ConfirmationTokenService confirmationTokenService;

    @Value("${server.hostPath}")
    private String host;


    /**
     * <p>The method registers credentials and sends an email to the user to verify the account.</p>
     *
     * @param credential the credential to be registered and confirmed in the system
     * @return the token that is sent to user's email
     * @throws IllegalStateException if the email is not valid
     */
    public String register(Credential credential) {
        boolean isValidEmail = emailValidator
                .verifyEmail(credential.getUsername());
        if (!isValidEmail) {
            throw new IllegalStateException("Email is not valid!");
        }
        String token = credentialService.signUpCredential(credential);
        String link = host + "/auth/confirmation?token=" + token;
        senderEmailService.send(
                credential.getUsername(),
                link
        );
        return token;
    }

    /**
     * Confirms token and enables account
     *
     * @param token token to confirm
     * @return a "confirmed" message to inform user that the confirmation is complete
     */
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        credentialService.enableCredential(
                confirmationToken.getCredential().getId());
        return "confirmed";
    }
}
