package io.github.thefive40.tienda_front.controller.main.menu.client.popups;
import io.github.thefive40.tienda_front.controller.main.menu.client.ClientController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.notifications.information.AlertRegister;
import io.github.thefive40.tienda_front.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class RegisterClientController implements Initializable {

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtCargo;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtLastName;

    @FXML
    private PasswordField txtPassword;

    private AuthService authService;

    private ClientController clientController;
    @Autowired
    public void setClientController ( ClientController clientController ) {
        this.clientController = clientController;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }

    @FXML
    void handleRegisterClient () {
        ClientDTO client = new ClientDTO ( );
        client.setName ( txtName.getText ( ) );
        client.setLastname ( txtLastName.getText ( ) );
        client.setEmail ( txtEmail.getText ( ) );
        client.setUrl ( "/static/media/images/util/profile.jpeg" );
        client.setPassword ( txtPassword.getText () );
        client.setPhone ( txtTel.getText () );
        client.setRole ( txtCargo.getText ( ) );
        authService.commit ();
        authService.register ( client );
        new AlertRegister ().showClientRegister ();
        clientController.refresh ();

    }

    @Autowired
    public void inject ( AuthService authService ) {
        this.authService = authService;
    }
}
