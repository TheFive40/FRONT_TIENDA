package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.model.dto.UserDTO;
import io.github.thefive40.tienda_front.repository.AuthRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AuthService {

    private AuthRepository authRepository;

    /**
     * @param authRepository
     */
    @Autowired
    private void setAuthRepository ( AuthRepository authRepository ) {
        this.authRepository = authRepository;
    }

    public boolean login ( String email, String password ) {
        return authRepository.sendLogin ( email, password );
    }

    public void register ( UserDTO userDTO ) {
        if (authRepository.isCommit ()){
            authRepository.uncommit ();
            authRepository.sendRegistration ( userDTO );
        }
    }
    public boolean isCommit(){
        return authRepository.isCommit();
    }
    public void commit(){
        authRepository.commit ();
    }
}
