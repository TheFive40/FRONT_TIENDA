package io.github.thefive40.tienda_front.controller.main;

import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ClientController implements Initializable {

    @FXML
    private Label clientRol5;

    @FXML
    private Label clientRol4;

    @FXML
    private ImageView clientImg1;

    @FXML
    private Label clientEmail5;

    @FXML
    private ImageView clientImg2;

    @FXML
    private ImageView clientImg3;

    @FXML
    private Label clientEmail2;

    @FXML
    private ImageView clientImg4;

    @FXML
    private Button columnRol;

    @FXML
    private Label clientEmail1;

    @FXML
    private ImageView clientImg5;

    @FXML
    private Label clientEmail4;

    @FXML
    private Label clientEmail3;

    @FXML
    private Button columnOpciones;

    @FXML
    private Label clientRol1;

    @FXML
    private Label clientRol3;

    @FXML
    private Label clientRol2;

    @FXML
    private Button btnAfter;

    @FXML
    private Label idClient1;

    @FXML
    private Button btnLogout;

    @FXML
    private Label idClient5;

    @FXML
    private Label idClient4;

    @FXML
    private Label idClient3;

    @FXML
    private Label idClient2;

    @FXML
    private Button columnEmail;

    @FXML
    private AnchorPane homeParent;

    @FXML
    private HBox containerLogout;

    @FXML
    private TextField page;

    @FXML
    private Button columnImagen;

    @FXML
    private Button clientRemove1;

    @FXML
    private Button searchButton;

    @FXML
    private Button clientRemove2;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Button clientRemove3;

    @FXML
    private Button clientRemove4;

    @FXML
    private Button columnId;

    @FXML
    private Button clientRemove5;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label clientName3;

    @FXML
    private Label clientTel5;

    @FXML
    private Button clientEdit5;

    @FXML
    private Label clientName2;

    @FXML
    private Label clientTel4;

    @FXML
    private Label clientName5;

    @FXML
    private Label clientName4;

    @FXML
    private Label clientTel1;

    @FXML
    private Label clientName1;

    @FXML
    private Label clientTel3;

    @FXML
    private Label clientTel2;

    @FXML
    private Label userName;

    @FXML
    private Button clientEdit1;

    @FXML
    private Button clientEdit2;

    @FXML
    private Button clientEdit3;

    @FXML
    private Button clientEdit4;

    @FXML
    private Button columnTel;

    @FXML
    private Label userRole;

    @FXML
    private Button btnBefore;

    @FXML
    private ComboBox<?> filterButton;

    @FXML
    private Button columnName;

    @FXML
    private VBox vboxContainer;

    private UserService userService;

    private Stage stage;

    private SignUpController signUp;

    private LoginController login;

    private UtilityService utilityService;

    @Autowired
    public void inject ( UserService service, Stage stage, LoginController login, SignUpController signUpController
            , UtilityService utilityService ) {
        this.userService = service;
        this.stage = stage;
        this.login = login;
        this.signUp = signUpController;
        this.utilityService = utilityService;
    }

    @FXML
    void handleEditClient1 ( ActionEvent event ) {

    }

    @FXML
    void handleRemoveClient1 ( ActionEvent event ) {

    }

    @FXML
    void handleEditClient2 ( ActionEvent event ) {

    }

    @FXML
    void handleRemoveClient2 ( ActionEvent event ) {

    }

    @FXML
    void handleEditClient3 ( ActionEvent event ) {

    }

    @FXML
    void handleRemoveClient3 ( ActionEvent event ) {

    }

    @FXML
    void handleEditClient4 ( ActionEvent event ) {

    }

    @FXML
    void handleRemoveClient4 ( ActionEvent event ) {

    }

    @FXML
    void handleEditClient5 ( ActionEvent event ) {

    }

    @FXML
    void handleRemoveClient5 ( ActionEvent event ) {

    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        List<ClientDTO> clients = userService.findAll ( );
        fillTableClients ( clients );
    }

    public void fillTableClients ( List<ClientDTO> clients ) {
        List<ClientDTO> clientDTOS = utilityService.getClientsByPage ( 1, clients );
        AtomicInteger contador = new AtomicInteger ( 0 );
        vboxContainer.getChildren ( ).forEach ( e -> {
            Circle clip = new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                    Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) );
            int count = contador.getAndIncrement ( );
            HBox container = (HBox) e;
            ImageView imageView = (ImageView) container.getChildren ( ).get ( 0 );
            Label idLabel = (Label) container.getChildren ( ).get ( 1 );
            Label nameLabel = (Label) container.getChildren ( ).get ( 2 );
            Label emailLabel = (Label) container.getChildren ( ).get ( 3 );
            Label telLabel = (Label) container.getChildren ( ).get ( 4 );
            Label rolLabel = (Label) container.getChildren ( ).get ( 5 );
            Button buttonEdit = (Button) container.getChildren ( ).get ( 6 );
            Button buttonRemove = (Button) container.getChildren ( ).get ( 7 );
            if (count >= clientDTOS.size ( )) {
                idLabel.setText ( "" );
                nameLabel.setText ( "" );
                emailLabel.setText ( "" );
                telLabel.setText ( "" );
                rolLabel.setText ( "" );
                imageView.setImage ( null );
                buttonEdit.setVisible ( false );
                buttonRemove.setVisible ( false );
            } else {
                ClientDTO client = clientDTOS.get ( count );
                imageView.setImage ( new Image ( client.getUrl ( ) ) );
                idLabel.setText ( String.valueOf ( client.getIdClient ( ) ) );
                nameLabel.setText ( client.getName ( ) );
                emailLabel.setText ( client.getEmail ( ) );
                telLabel.setText ( client.getPhone ( ) );
                rolLabel.setText ( "Administrador" );
                imageView.setClip ( clip );
                buttonEdit.setVisible ( true );
                buttonRemove.setVisible ( true );
            }

        } );
    }
}
