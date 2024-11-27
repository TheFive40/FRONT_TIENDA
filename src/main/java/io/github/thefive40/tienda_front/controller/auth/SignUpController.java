package io.github.thefive40.tienda_front.controller.auth;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.config.notifications.error.AuthError;
import io.github.thefive40.tienda_front.service.EmailService;
import io.github.thefive40.tienda_front.service.AuthValidatorService;
import io.github.thefive40.tienda_front.service.UserRegistrationService;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * SignUpController handles the registration functionality for the application.
 * It manages form validation, user creation, and verification workflows.
 */
@Component("SignUpController")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SignUpController implements Initializable {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private Label lblConfirmPassword;
    @FXML
    private Label lblPassword;
    @FXML
    private PasswordField confirmPasswordField;

    private Stage stage;

    protected Stage prototypeStage;

    private EmailService emailService;

    private UserRegistrationService userRegistrationService;

    private AuthValidatorService authValidateService;

    private ApplicationContext context;

    private AuthError authError;
    @Setter
    @Getter
    private ClientDTO currentUser;
    @Autowired
    private UserService userService;



    /**
     * Initializes the controller after the FXML file is loaded.
     *
     * @param url            the location used to resolve relative paths.
     * @param resourceBundle the resources used to localize the root object.
     */
    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }
    /**
     * Handles the user registration process.
     * Validates the user input, registers the user, and initiates email verification.
     *
     * @throws MessagingException if there is an error sending the verification email.
     */
    @FXML
    void handleSubmit () throws MessagingException {
        currentUser = new ClientDTO (
                emailField.getText ( ),
                passwordField.getText ( ),
                firstNameField.getText ( ),
                lastNameField.getText ( ),
                phoneNumberField.getText ( ) );
        if(validateUser ( currentUser )){
            userRegistrationService.registerUser ( currentUser );
            initVerificationStage ( );
            emailService.sendVerificationCode ( emailField.getText ( ) );
        }
    }


    /**
     * Validates the password as the user types, updating the label with strength feedback.
     *
     * @param event the key event triggering the validation.
     */
    @FXML
    void handlePasswordEvent ( KeyEvent event ) {
        String text = passwordField.getText ( ) + event.getText ( );
        if (authValidateService.isStrongPassword ( text )) {
            lblPassword.setText ( "Strong" );
            lblPassword.setTextFill ( Color.RED );
            return;
        }
        lblPassword.setText ( "Easy" );
        lblPassword.setTextFill ( Color.LIGHTGREEN );
    }

    /**
     * Validates the password confirmation field as the user types, updating the label with feedback.
     *
     * @param event the key event triggering the validation.
     */
    @FXML
    void handleConfirmPasswordEvent ( KeyEvent event ) {
        String text = confirmPasswordField.getText ( ) + event.getText ( );
        if (passwordField.getText ( ).equals ( text )) {
            lblConfirmPassword.setText ( "Correct" );
            lblConfirmPassword.setTextFill ( Color.LIGHTGREEN );
            return;
        }
        lblConfirmPassword.setText ( "Incorrect" );
        lblConfirmPassword.setTextFill ( Color.RED );
    }
    /**
     * Transitions back to the login view.
     *
     * @throws IOException if the scene transition fails.
     */
    @FXML
    void handleBackToLogin () throws IOException {
        stage.setScene ( new Scene ( context.getBean ( "loginParent", AnchorPane.class ) ) );
    }

    /**
     * Initializes and displays the verification stage.
     */
    private void initVerificationStage () {
        prototypeStage.initStyle ( StageStyle.UNDECORATED );
        prototypeStage.setScene ( new Scene ( context.getBean ( "verificationParent", VBox.class ) ) );
        prototypeStage.show ( );
    }
    /**
     * Validates the user's credentials.
     *
     * @param clientDTO the {@link ClientDTO} containing the user's information.
     * @return {@code true} if the user is valid; {@code false} otherwise.
     */
    protected boolean validateUser ( ClientDTO clientDTO ) {
        if (!           authValidateService.isCredentialsValid ( clientDTO )) {
            authError.show ( );
            return false;
        }
        return true;
    }

    /**
     * Injects the main and prototype stages.
     *
     * @param stage           the main stage.
     * @param prototypeStage  the prototype stage for verification.
     */
    @Autowired
    public void injectWindows ( Stage stage , Stage prototypeStage ) {
        this.stage = stage;
        this.prototypeStage = prototypeStage;
    }
    /**
     * Injects the application context and authentication error components.
     *
     * @param context   the {@link ApplicationContext} for accessing beans.
     * @param authError the {@link AuthError} for handling authentication errors.
     */
    @Autowired
    public void inject ( ApplicationContext context, AuthError authError ) {
        this.authError = authError;
        this.context = context;
    }
    /**
     * Injects the email, registration, and validation services.
     *
     * @param emailService            the {@link EmailService} for email-related operations.
     * @param userRegistrationService the {@link UserRegistrationService} for user registration.
     * @param authValidateService     the {@link AuthValidatorService} for authentication validation.
     */
    @Autowired
    public void injectService ( EmailService emailService, UserRegistrationService userRegistrationService,
                                AuthValidatorService authValidateService ) {
        this.emailService = emailService;
        this.userRegistrationService = userRegistrationService;
        this.authValidateService = authValidateService;
    }
    /**
     * Sets the user service instance.
     *
     * @param userService the {@link UserService} for managing user operations.
     */
    @Autowired
    public void setUserService ( UserService userService ) {
        this.userService = userService;
    }
}
