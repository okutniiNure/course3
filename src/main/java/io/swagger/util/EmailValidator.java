package io.swagger.util;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {

    private final String emailVerifierRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)" +
            "*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    /**
     * The following restrictions are imposed in the email addresses local-part:
     * <ul>
     *    <li>It allows numeric values from 0 to 9
     *    <li>Both uppercase and lowercase letters from a to z are allowed
     *    <li>Allowed are underscore “_”, hyphen “-” and dot “.”
     *    <li>Dot isn't allowed at the start and end of the local-part
     *    <li>Consecutive dots aren't allowed
     *    <li>For the local part, a maximum of 64 characters are allowed
     * </ul>
     * Restrictions for the domain-part:
     * <ul>
     *    <li>It allows numeric values from 0 to 9
     *    <li>We allow both uppercase and lowercase letters from a to z
     *    <li>Hyphen “-” and dot “.” isn't allowed at the start and end of the domain-part
     *    <li>No consecutive dots
     * </ul>
     *
     * @param email string to be tested
     * @return if string satisfies the conditions to be an email returns true, else returns false
     */
    public boolean verifyEmail(String email) {
        return Pattern.compile(emailVerifierRegex)
                .matcher(email)
                .matches();
    }
}
