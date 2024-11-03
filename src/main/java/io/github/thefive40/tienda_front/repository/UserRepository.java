package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    ClientDTO getUserByEmail ( String email );
    String getPasswordByEmail ( String email );
    String getEmailByPhone ( String phone );
}
