package io.github.thefive40.tienda_front.controller.auth;

import io.github.thefive40.tienda_front.model.dto.UserDTO;
import io.github.thefive40.tienda_front.notifications.error.AuthError;
import io.github.thefive40.tienda_front.service.AuthService;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component("LoginController")
@Lazy
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LoginController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane loginParent;
    private Stage stage;
    private AnchorPane registerParent;
    private Logger logger;
    private ApplicationContext context;
    private AuthService authService;
    private UserService userService;
    private AuthError authError;
    @Getter
    private UserDTO currentUser;


    @Autowired
    private void setParents ( AnchorPane registerParent, Logger logger, Stage stage,
                              ApplicationContext context, AuthError authError ) {
        this.registerParent = registerParent;
        this.logger = logger;
        this.stage = stage;
        this.context = context;
        this.authError = authError;
    }

    @Autowired
    private void injectServices ( AuthService authService, UserService userService ) {
        this.authService = authService;
        this.userService = userService;
    }

    public void handleLogin () {
        if (authService.login ( emailField.getText ( ), passwordField.getText ( ) )) {
            currentUser = userService.getUserByEmail ( emailField.getText ( ) );
            stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
            return;
        }
        authError.show ( );
        logger.info ( "Invalid credentials for user: {}", emailField.getText ( ) );
    }

    public void handleForgotPassword ( ActionEvent event ) {
    }

    public void handleRegister () throws IOException {
        stage.setScene ( new Scene ( context.getBean ( "registerParent", AnchorPane.class ) ) );
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }
}
