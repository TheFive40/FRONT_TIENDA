package io.github.thefive40.tienda_front.service;

import io.github.thefive40.tienda_front.model.dto.UserDTO;
import io.github.thefive40.tienda_front.repository.AuthValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class AuthValidatorService implements AuthValidatorRepository {

    private UserDTO userDTO;

    @Autowired
    private void inject ( UserDTO userDTO ) {
        this.userDTO = userDTO;
    }

    @Autowired
    private Validator validator;

    public boolean isStrongPassword ( String password ) {
        userDTO.setPassword ( password );
        BindingResult bindingResult = new BeanPropertyBindingResult ( userDTO, "userDTO" );
        validator.validate ( userDTO, bindingResult );
        return !bindingResult.hasFieldErrors ( "password" );
    }

    @Override
    public boolean isCredentialsValid ( UserDTO userDTO ) {
        BindingResult bindingResult = new BeanPropertyBindingResult ( userDTO, "userDTO" );
        validator.validate ( userDTO, bindingResult );
        return !bindingResult.hasErrors ();
    }

    @Override
    public boolean isAdmin ( String username ) {
        return false;
    }
}
