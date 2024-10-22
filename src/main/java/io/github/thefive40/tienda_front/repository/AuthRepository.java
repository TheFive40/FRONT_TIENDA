package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {
    boolean sendLogin ( String email, String password );
    boolean sendRegistration ( UserDTO userDTO );
    void commit();
    boolean isCommit();
    void setCommit( boolean commit );
}
