package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Credential;
import io.swagger.security.auth.RegistrationService;
import io.swagger.service.auth.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthApiController implements AuthApi {

    private final AuthService authService;

    private final RegistrationService registrationService;

    /**
     * Creates account in the system and sends confirmation email to the user
     *
     * @param body     credential body
     * @param response http response
     */
    public void createAccount(Credential body, HttpServletResponse response) {
        authService.createAccount(body);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * @param token confirmation token
     * @return a "confirmed" message to inform user that the confirmation is complete
     */
    @Override
    public String confirmAccount(String token) {
        return registrationService.confirmToken(token);
    }

    /**
     * @param authorization authorization bearer
     * @param accountId     received account ID
     * @return credential by id
     */
    @Override
    public Credential getAccountById(String authorization, Long accountId) {
        return authService.fetchAccountById(accountId);
    }

    /**
     * @param authorization authorization bearer
     * @param accountId     updated account ID
     * @param body          updated credential's information
     */
    public void updateAccountById(String authorization, Long accountId, Credential body) {
        authService.updateAccountById(accountId, body);
    }

    /**
     * @param authorization authorization bearer
     * @param accountId     deleted account ID
     */
    public void deleteAccountById(String authorization, Long accountId) {
        authService.deleteById(accountId);
    }

    /**
     * Gets information of logged-in user credentials
     *
     * @param authorization authorization bearer
     * @return logged in user credentials
     */
    public Credential getLoginedAccount(String authorization) {
        return authService.fetchLoginedAccount();
    }

    /**
     * Updates information of logged-in user credentials
     *
     * @param authorization authorization bearer
     * @param body          updated credential's information
     */
    @Override
    public void updateLoginedAccount(String authorization, Credential body) {
        authService.updateLoginedAccount(body);
    }

    /**
     * Deletes information of logged-in user by credentials
     *
     * @param authorization authorization bearer
     */
    @Override
    public void deleteLoginedAccount(String authorization) {
        authService.deleteLoginedAccount();
    }
}
