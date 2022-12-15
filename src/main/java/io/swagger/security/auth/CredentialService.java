package io.swagger.security.auth;

import io.swagger.model.Credential;
import io.swagger.repository.CredentialRepository;
import io.swagger.security.auth.token.ConfirmationToken;
import io.swagger.service.auth.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService implements UserDetailsService {

    private final CredentialRepository credentialRepository;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    /**
     * <p>The method creates and stores a confirmation token in the database that expires after 15 minutes for the credentials in the parameters.</p>
     *
     * @param credential object to be signed
     * @return string representing token for the credential to be signed
     */
    public String signUpCredential(Credential credential) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                credential
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    /**
     * Enables credential by ID
     *
     * @param id credential id
     */
    public void enableCredential(Long id) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("token leads nowhere, shit happens..."));
        credential.setAccountNonLocked(true);
        credential.setEnabled(true);
        credentialRepository.save(credential);
    }
}
