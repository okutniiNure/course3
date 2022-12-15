package io.swagger.service.auth;

import io.swagger.model.Credential;
import io.swagger.repository.CredentialRepository;
import io.swagger.security.auth.ApplicationUserRole;
import io.swagger.security.auth.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;
    private final HeaderService headerService;

    /**
     * Creates account in the system and sends confirmation email to the user
     *
     * @param body credential body
     */
    @Transactional
    public void createAccount(Credential body) {
        if (headerService.getUsername() != null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are already logged in");
        if (credentialRepository.existsByUsername(body.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        if (body.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Role is wrong. Please use one of these: %s",
                            Arrays.stream(ApplicationUserRole.values())
                                    .map(Enum::toString)
                                    .collect(Collectors.toSet())));
        }
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        credentialRepository.save(body);
        registrationService.register(body);
    }

    /**
     * @param accountId received account ID
     * @return credential by id
     */
    public Credential fetchAccountById(Long accountId) {
        return credentialRepository
                .findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such credential with ID " + accountId));
    }

    /**
     * @param accountId updated account ID
     * @param body      updated credential's information
     */
    @Transactional
    public void updateAccountById(Long accountId, Credential body) {
        Credential credential = credentialRepository
                .findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such credential with ID " + accountId));

        if (!credential.getUsername().equals(body.getUsername()) && credentialRepository.existsByUsername(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already has been taken");
        }
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        credential.updateAccount(body);
        credentialRepository.save(credential);
    }

    /**
     * @param accountId deleted account ID
     */
    public void deleteById(Long accountId) {
        credentialRepository.deleteById(accountId);
    }

    /**
     * Gets information of logged-in user credentials
     *
     * @return logged in user credentials
     */
    public Credential fetchLoginedAccount() {
        String username = headerService.getUsername();

        return credentialRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such credential with username " + username));
    }

    /**
     * Updates information of logged-in user credentials
     *
     * @param body updated credential's information
     */
    @Transactional
    public void updateLoginedAccount(Credential body) {
        String username = headerService.getUsername();
        if (!username.equals(body.getUsername()) && credentialRepository.existsByUsername(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already has been taken");
        }
        Credential credential = credentialRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such credential with username " + username));

        body.setPassword(passwordEncoder.encode(body.getPassword()));
        credential.updateAccount(body);
        credentialRepository.save(credential);
    }

    /**
     * Deletes information of logged-in user by credentials
     */
    @Transactional
    public void deleteLoginedAccount() {
        String username = headerService.getUsername();
        Optional<Credential> credential = credentialRepository.findByUsername(username);

        credential.ifPresent(credentialRepository::delete);
    }
}
