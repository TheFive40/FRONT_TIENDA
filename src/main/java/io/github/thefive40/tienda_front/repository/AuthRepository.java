package io.github.thefive40.tienda_front.repository;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {
    boolean sendLogin ( String email, String password );
    void sendRegistration ( ClientDTO clientDTO );
    void commit();
    boolean isCommit();
    void uncommit ();
}
