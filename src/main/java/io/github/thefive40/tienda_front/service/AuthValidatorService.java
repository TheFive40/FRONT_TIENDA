package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.repository.AuthValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class AuthValidatorService implements AuthValidatorRepository {

    private ClientDTO clientDTO;

    @Autowired
    private void inject ( ClientDTO clientDTO ) {
        this.clientDTO = clientDTO;
    }

    @Autowired
    private Validator validator;

    public boolean isStrongPassword ( String password ) {
        clientDTO.setPassword ( password );
        BindingResult bindingResult = new BeanPropertyBindingResult ( clientDTO, "userDTO" );
        validator.validate ( clientDTO, bindingResult );
        return !bindingResult.hasFieldErrors ( "password" );
    }

    @Override
    public boolean isCredentialsValid ( ClientDTO clientDTO ) {
        BindingResult bindingResult = new BeanPropertyBindingResult ( clientDTO, "userDTO" );
        validator.validate ( clientDTO, bindingResult );
        return !bindingResult.hasErrors ();
    }

    @Override
    public boolean isAdmin ( String username ) {
        return false;
    }
}
