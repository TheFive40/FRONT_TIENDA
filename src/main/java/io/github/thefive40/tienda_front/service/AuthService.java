package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.repository.AuthRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * AuthService is a class that provides services related to user authentication
 * and client registration in the application. It uses an authentication repository
 * to interact with the persistence layer.
 */
@Service
@Getter
public class AuthService {

    /**
     * Repository to handle authentication operations.
     */
    private AuthRepository authRepository;

    /**
     * @param authRepository
     */

    /**
     * Sets the authentication repository to be used.
     *
     * @param authRepository an instance of {@link AuthRepository} injected by Spring.
     */
    @Autowired
    private void setAuthRepository ( AuthRepository authRepository ) {
        this.authRepository = authRepository;
    }
    /**
     * Logs in to the system using an email and password.
     *
     * @param email    the user's email address.
     * @param password the user's password.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     */
    public boolean login ( String email, String password ) {
        return authRepository.sendLogin ( email, password );
    }
    /**
     * Registers a new client in the system. Before registering, it checks
     * whether the repository is in a committed state. If so, it disables
     * the commit state and performs the registration.
     *
     * @param clientDTO the client's data encapsulated in a {@link ClientDTO} object.
     */
    public void register ( ClientDTO clientDTO ) {
        if (authRepository.isCommit ()){
            authRepository.uncommit ();
            authRepository.sendRegistration ( clientDTO );
        }
    }
    /**
     * Checks if the repository is in a committed state.
     * Returns true when the client passes all authentication
     * tests and is ready to enter the application
     * @return {@code true} if it is committed; {@code false} otherwise.
     */

    public boolean isCommit(){
        return authRepository.isCommit();
    }

    /**
     * Sets the repository to a committed state.
     */
    public void commit(){
        authRepository.commit ();
    }
}
