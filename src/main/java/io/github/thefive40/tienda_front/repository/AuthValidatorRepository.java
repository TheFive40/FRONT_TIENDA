package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthValidatorRepository {
    boolean isStrongPassword(String password);
    boolean isCredentialsValid( UserDTO userDTO );
    boolean isAdmin(String username);

}
