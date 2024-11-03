package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthValidatorRepository {
    boolean isStrongPassword(String password);
    boolean isCredentialsValid( ClientDTO clientDTO );
    boolean isAdmin(String username);

}
