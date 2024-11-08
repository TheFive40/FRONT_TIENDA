package io.github.thefive40.tienda_front.controller.main.client.popups;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.main.client.ClientController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.notifications.information.AlertRegister;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

@Component
public class ClientEditController implements Initializable {
    @FXML
    private TextField telefonoField;

    @FXML
    private ComboBox<String> rolComboBox;

    @FXML
    private TextField estadoField;

    @FXML
    private Button actualizarButton;

    @FXML
    private GridPane mainGrid;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    private ClientController clientController;

    private ApplicationContext context;

    private UserService userService;

    private ClientController client;


    @Autowired
    void inject ( ApplicationContext context, UserService authService ) {
        this.context = context;
        this.userService = authService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        client = context.getBean ( "clientController", ClientController.class );
        ClientDTO clientDTO = client.getClientEdit ( );
        nombreField.setText ( clientDTO.getName ( ) );
        apellidoField.setText ( clientDTO.getLastname ( ) );
        emailField.setText ( clientDTO.getEmail ( ) );
        telefonoField.setText ( clientDTO.getPhone ( ) );
        passwordField.setText ( clientDTO.getPassword ( ) );
        int index = Collections.binarySearch ( rolComboBox.getItems ( ), clientDTO.getRole ( ) );
        rolComboBox.selectionModelProperty ( ).get ( ).select ( index );
        estadoField.setText ( "Activo" );
    }

    @FXML
    void handleUpdate () throws JsonProcessingException {
        ClientDTO clientDTO = client.getClientEdit ( );
        clientDTO.setIdClient ( client.getClientEdit ( ).getIdClient ( ) );
        clientDTO.setRole ( rolComboBox.getSelectionModel ( ).getSelectedItem ( ) );
        clientDTO.setName ( nombreField.getText ( ) );
        clientDTO.setLastname ( apellidoField.getText ( ) );
        clientDTO.setPhone ( telefonoField.getText ( ) );
        clientDTO.setEmail ( emailField.getText ( ) );
        clientDTO.setPassword ( passwordField.getText ( ) );
        userService.update ( clientDTO );
        new AlertRegister ().showUpdateClient ();
    }
}
