package io.github.thefive40.tienda_front.controller.auth;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.config.notifications.error.AuthError;
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


/**
 * LoginController handles the login functionality for the application.
 * It manages user authentication, scene transitions, and error handling.
 */
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
    private ClientDTO currentUser;
    /**
     * Retrieves the current user from the database.
     *
     * @return a {@link ClientDTO} representing the current user.
     */
    public ClientDTO getCurrentUser () {
        return userService.getUserByEmail ( currentUser.getEmail ( ) );
    }
    /**
     * Sets various dependencies for the controller.
     *
     * @param registerParent the parent AnchorPane for the registration view.
     * @param logger         the {@link Logger} instance for logging.
     * @param stage          the main application stage.
     * @param context        the {@link ApplicationContext} for accessing beans.
     * @param authError      the {@link AuthError} for displaying login errors.
     */
    @Autowired
    private void setParents ( AnchorPane registerParent, Logger logger, Stage stage,
                              ApplicationContext context, AuthError authError ) {
        this.registerParent = registerParent;
        this.logger = logger;
        this.stage = stage;
        this.context = context;
        this.authError = authError;
    }
    /**
     * Injects the authentication and user services into the controller.
     *
     * @param authService the {@link AuthService} for authentication logic.
     * @param userService the {@link UserService} for user-related operations.
     */
    @Autowired
    private void injectServices ( AuthService authService, UserService userService ) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Handles the login action triggered by the user.
     * Authenticates the user and transitions to the appropriate home scene
     * based on their role.
     */
    @FXML
    public void handleLogin () {
        if (authService.login ( emailField.getText ( ), passwordField.getText ( ) )) {
            currentUser = userService.getUserByEmail ( emailField.getText ( ) );

            switch (currentUser.getRole ().toUpperCase ()){
                case "ADMINISTRADOR"->{
                    stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
                }
                case "CLIENTE"->{
                    stage.setScene ( new Scene ( context.getBean ( "homeClientParent", AnchorPane.class ) ) );
                }
                case "VENDEDOR"->{
                    stage.setScene ( new Scene ( context.getBean ( "homeSellerParent", AnchorPane.class ) ) );
                }
            }
            return;
        }
        authError.show ( );
        logger.info ( "Invalid credentials for user: {}", emailField.getText ( ) );
    }
    /**
     * Handles the forgot password action (currently not implemented).
     *
     * @param event the action event triggering the method.
     */
    public void handleForgotPassword ( ActionEvent event ) {
    }
    /**
     * Handles the register action, transitioning to the registration scene.
     *
     * @throws IOException if the scene transition fails.
     */
    public void handleRegister () throws IOException {
        stage.setScene ( new Scene ( context.getBean ( "registerParent", AnchorPane.class ) ) );
    }

    /**
     * Initializes the controller after the FXML file is loaded.
     *
     * @param url            the location used to resolve relative paths.
     * @param resourceBundle the resources used to localize the root object.
     */
    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }
}
