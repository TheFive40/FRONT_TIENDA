package io.github.thefive40.tienda_front.controller.auth;

import io.github.thefive40.tienda_front.service.AuthService;
import io.github.thefive40.tienda_front.service.EmailService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * VerificationCodeController manages the email verification process.
 * It validates the verification code entered by the user and transitions to the home view upon successful verification.
 */
@Component
@NoArgsConstructor
public class VerificationCodeController implements Initializable {
    @FXML
    private TextField verificationCodeField;
    @FXML
    private VBox rootPane;
    private Stage stage;
    private EmailService service;
    private AuthService authService;
    private ApplicationContext context;
    /**
     * Injects the required dependencies into the controller.
     *
     * @param stage       the main application stage.
     * @param service     the {@link EmailService} for email operations.
     * @param authService the {@link AuthService} for authentication management.
     * @param context     the {@link ApplicationContext} for accessing beans.
     */
    @Autowired
    private void inject ( Stage stage, EmailService service, AuthService authService, ApplicationContext context) {
        this.service = service;
        this.context = context;
        this.stage = stage;
        this.authService = authService;
    }
    /**
     * Handles the verification process.
     * <p>
     * Compares the entered verification code with the one sent via email. If the code matches, commits the
     * authentication and transitions to the home view.
     *
     * @throws MessagingException if there is an error during the verification process.
     */
    public void handleVerify () throws MessagingException {
        if ( service.getCode ( ).equals ( verificationCodeField.getText ( ) )) {
            authService.commit ();
            stage.setScene ( new Scene ( context.getBean ( "homeParent",AnchorPane.class ) ) );
            rootPane.getScene ().getWindow ().hide ();
        }

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
