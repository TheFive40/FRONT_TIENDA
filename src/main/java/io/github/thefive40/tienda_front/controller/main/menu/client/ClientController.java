package io.github.thefive40.tienda_front.controller.main.menu.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component("clientController")
@Getter
public class ClientController implements Initializable {

    @FXML
    private Button btnLogout;

    @FXML
    private TextField txtPage;
    @FXML
    private TextField txtTotalPage;

    @FXML
    private Button searchButton;


    @FXML
    private ImageView imgProfile;


    @FXML
    private Button columnId;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label userName;

    @FXML
    private Button btnInicio;

    @FXML
    private Label userRole;

    @FXML
    private Button btnBefore;

    @FXML
    private ComboBox<String> filterButton;

    @FXML
    private Button columnName;

    @FXML
    private VBox vboxContainer;

    private UserService userService;

    private Stage stage;

    private SignUpController signUp;

    private LoginController login;

    private UtilityService<ClientDTO> utilityService;

    private List<ClientDTO> clients;

    private ApplicationContext context;

    private Logger logger;

    private ClientDTO clientEdit;

    @Autowired
    public void inject ( UserService service, Stage stage, LoginController login, SignUpController signUpController
            , UtilityService<ClientDTO> utilityService, ApplicationContext context ) {
        this.userService = service;
        this.stage = stage;
        this.login = login;
        this.signUp = signUpController;
        this.utilityService = utilityService;
        this.context = context;
    }

    public void refresh () {
        clients = userService.findAll ( );
        fillTableClients ( clients );
    }

    @FXML
    void handleEditClient ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        clientEdit = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "clientEdit", GridPane.class ) ) );
        stage.setTitle ( "Actualizar" );
        stage.show ( );
    }
    @FXML
    void handleInvoice(){
        stage.setScene ( new Scene ( context.getBean ( "invoiceParent" , AnchorPane.class) ) );
    }
    @FXML
    void handleButtonRemove ( ActionEvent event ) throws JsonProcessingException {
        Button button = (Button) event.getSource ( );
        ClientDTO clientRemove = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        clientRemove.setStatus ( false );
        userService.update ( clientRemove );
        refresh ( );

    }

    @FXML
    void handleMenuInicio () {
        stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleLogout(){
        stage.setScene ( new Scene ( context.getBean ( "loginParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleClientRegister () {
        Stage root_stage = new Stage ( );
        root_stage.setScene ( new Scene ( context.getBean ( "clientRegister", AnchorPane.class ) ) );
        root_stage.setTitle ( "Registrar Cliente" );
        root_stage.show ( );
    }

    @FXML
    void handlePressed ( KeyEvent keyEvent ) {
        if (keyEvent.getCode ( ).equals ( KeyCode.ENTER )) {
            String item = filterButton.getSelectionModel ().getSelectedItem ();
            List<ClientDTO> clientDTOS;
            ClientDTO clientDTO = null;
            if (!searchTextField.getText ( ).isEmpty ( )) {
                if (item.equalsIgnoreCase ( "email" )){
                    clientDTO = userService.getUserByEmail ( searchTextField.getText ( ) );
                    if (clientDTO == null)
                        fillTableClients ( List.of ( ) );
                    else fillTableClients ( List.of ( clientDTO ) );
                }else if(item.equalsIgnoreCase ( "Apellidos" )){
                    clientDTOS = userService.findByLastName ( searchTextField.getText ( ) );
                    fillTableClients ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
                }else if(item.equalsIgnoreCase ( "Nombres" )){
                    clientDTOS = userService.findClientsByName ( searchTextField.getText () );
                    fillTableClients ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
                }

            } else {
                refresh ( );
            }
        }
    }

    @FXML
    void handleSearch () {
        if (!searchTextField.getText ( ).isEmpty ( )) {
            ClientDTO client = null;
            List<ClientDTO> clientDTOS;
            String item = filterButton.getSelectionModel ().getSelectedItem ();
            if (item.equalsIgnoreCase ( "email" )){
                client = userService.getUserByEmail ( searchTextField.getText ( ) );
                if (client == null)
                    fillTableClients ( List.of ( ) );
                else
                    fillTableClients ( List.of ( client ) );
            }else if(item.equalsIgnoreCase ( "Apellidos" )){
                clientDTOS = userService.findByLastName ( searchTextField.getText ( ) );
                fillTableClients ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
            }else if(item.equalsIgnoreCase ( "Nombres" )){
                clientDTOS = userService.findClientsByName ( searchTextField.getText () );
                fillTableClients ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
            }

        } else {
            refresh ( );
        }
    }

    @FXML
    void handleBefore () {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && 1 <
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) - 1) + "" );

        }
        refresh ( );
    }

    @FXML
    void handleAfter () {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && utilityService.totalPages ( clients ) >
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) + 1) + "" );
        }
        refresh ( );
    }

    @FXML
    void handleMenuProductos () {
        stage.setScene ( new Scene ( context.getBean ( "productParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleMenuCompra(){
        stage.setScene ( new Scene ( context.getBean ( "purchaseParent", AnchorPane.class ) ) );
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        clients = userService.findAll ( );
        txtTotalPage.setText ( utilityService.totalPages ( clients ) + "" );
        txtPage.setText ( "1" );
        fillTableClients ( clients );
        imgProfile.setClip ( new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) ) );
        if (login.getCurrentUser ( ) != null)
            userName.setText ( login.getCurrentUser ( ).getName ( ) );
        else userName.setText ( signUp.getCurrentUser ( ).getName ( ) );
        userRole.setText ( "ADMINISTRADOR" );
        filterButton.getItems ().addAll ( "Email", "Nombres", "Apellidos" );
        filterButton.getSelectionModel ().select ( 0 );
    }

    public void fillTableClients ( List<ClientDTO> clients ) {
        clients = clients.stream ( ).filter ( ClientDTO::isStatus ).toList ( );
        List<ClientDTO> clientDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), clients );
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
                clearUserInfoFields ( idLabel, nameLabel, emailLabel, telLabel,
                        rolLabel, imageView, buttonEdit, buttonRemove );
            } else {
                ClientDTO client = clientDTOS.get ( count );
                if (client.isStatus ( )) {
                    imageView.setImage ( new Image ( client.getUrl ( ) ) );
                    idLabel.setText ( String.valueOf ( client.getIdClient ( ) ) );
                    nameLabel.setText ( client.getName ( ) );
                    emailLabel.setText ( client.getEmail ( ) );
                    telLabel.setText ( client.getPhone ( ) );
                    rolLabel.setText ( client.getRole ( ) );
                    imageView.setClip ( clip );
                    buttonEdit.setVisible ( true );
                    buttonRemove.setVisible ( true );
                }
            }
        } );
    }

    void clearUserInfoFields ( Label idLabel, Label nameLabel, Label emailLabel, Label telLabel,
                               Label rolLabel, ImageView imageView, Button buttonEdit, Button buttonRemove ) {
        idLabel.setText ( "" );
        nameLabel.setText ( "" );
        emailLabel.setText ( "" );
        telLabel.setText ( "" );
        rolLabel.setText ( "" );
        imageView.setImage ( null );
        buttonEdit.setVisible ( false );
        buttonRemove.setVisible ( false );

    }

    @Autowired
    public void setLogger ( Logger logger ) {
        this.logger = logger;
    }
}
