package io.github.thefive40.tienda_front.controller.auth;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.config.notifications.error.AuthError;
import io.github.thefive40.tienda_front.service.EmailService;
import io.github.thefive40.tienda_front.service.AuthValidatorService;
import io.github.thefive40.tienda_front.service.UserRegistrationService;
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
    @Getter
    @Setter
    private ClientDTO currentUser;

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }

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

    @FXML
    void handleBackToLogin () throws IOException {
        stage.setScene ( new Scene ( context.getBean ( "loginParent", AnchorPane.class ) ) );
    }

    private void initVerificationStage () {
        prototypeStage.initStyle ( StageStyle.UNDECORATED );
        prototypeStage.setScene ( new Scene ( context.getBean ( "verificationParent", VBox.class ) ) );
        prototypeStage.show ( );
    }

    protected boolean validateUser ( ClientDTO clientDTO ) {
        if (!           authValidateService.isCredentialsValid ( clientDTO )) {
            authError.show ( );
            return false;
        }
        return true;
    }

    @Autowired
    public void injectWindows ( Stage stage , Stage prototypeStage ) {
        this.stage = stage;
        this.prototypeStage = prototypeStage;
    }

    @Autowired
    public void inject ( ApplicationContext context, AuthError authError ) {
        this.authError = authError;
        this.context = context;
    }

    @Autowired
    public void injectService ( EmailService emailService, UserRegistrationService userRegistrationService,
                                AuthValidatorService authValidateService ) {
        this.emailService = emailService;
        this.userRegistrationService = userRegistrationService;
        this.authValidateService = authValidateService;
    }
}
