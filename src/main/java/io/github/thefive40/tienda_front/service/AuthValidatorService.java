package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.repository.AuthValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
/**
 * AuthValidatorService is a service class responsible for validating client credentials
 * and ensuring that passwords meet security requirements. It uses Spring's validation
 * framework to enforce constraints on the provided data.
 */
@Service
public class AuthValidatorService implements AuthValidatorRepository {

    /**
     * The ClientDTO instance used for validation operations.
     */
    private ClientDTO clientDTO;
    /**
     * Spring's validator for performing validation on objects.
     */
    @Autowired
    private Validator validator;

    /**
     * Injects the {@link ClientDTO} object used for password validation.
     *
     * @param clientDTO an instance of {@link ClientDTO} injected by Spring.
     */
    @Autowired
    private void inject ( ClientDTO clientDTO ) {
        this.clientDTO = clientDTO;
    }

    /**
     * Validates whether the provided password meets the security criteria.
     *
     * @param password the password to validate.
     * @return {@code true} if the password is strong and meets validation criteria;
     *         {@code false} otherwise.
     */
    public boolean isStrongPassword ( String password ) {
        clientDTO.setPassword ( password );
        BindingResult bindingResult = new BeanPropertyBindingResult ( clientDTO, "userDTO" );
        validator.validate ( clientDTO, bindingResult );
        return !bindingResult.hasFieldErrors ( "password" );
    }
    /**
     * Validates whether the provided client credentials are valid based on predefined constraints.
     *
     * @param clientDTO the {@link ClientDTO} object containing client credentials.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     */
    @Override
    public boolean isCredentialsValid ( ClientDTO clientDTO ) {
        BindingResult bindingResult = new BeanPropertyBindingResult ( clientDTO, "userDTO" );
        validator.validate ( clientDTO, bindingResult );
        return !bindingResult.hasErrors ();
    }
    /**
     * Checks whether the given username belongs to an administrator.
     *
     * @param username the username to check.
     * @return {@code true} if the username belongs to an administrator;
     *         {@code false} otherwise.
     */
    @Override
    public boolean isAdmin ( String username ) {
        return false;
    }
}
