package io.github.thefive40.tienda_front.controller.main.menu.invoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.InvoiceDTO;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component("InvoiceController")
@Getter
public class InvoiceController implements Initializable {
    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField txtTotalPage;

    @FXML
    private TextField txtPage;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label userName;

    @FXML
    private Label userRole;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnClientes;

    @FXML
    private HBox containerLogout;

    private HashMap<InvoiceDTO, ClientDTO> clientsInvoices;

    private final UtilityService<InvoiceDTO> utilityService;

    private final Stage stage;

    private UserService userService;

    private List<InvoiceDTO> invoiceDTOS;

    private InvoiceDTO currentInvoice;

    @Autowired
    private ApplicationContext context;
    @Autowired
    private ClientDTO clientDTO;

    public InvoiceController ( UtilityService<InvoiceDTO> utilityService, UserService userService, @Qualifier("stage") Stage stage ) {
        this.utilityService = utilityService;
        this.userService = userService;
        this.stage = stage;
    }


    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        //var client = context.getBean ( "HomeController", HomeController.class ).getCurrentUser ();
        clientsInvoices = new HashMap<> ( );
        var client = utilityService.getClientByRol ( );
        invoiceDTOS = new ArrayList<> ( );
        txtPage.setText ( "1" );
        switch (client.getRole ( ).toUpperCase ( )) {
            case "ADMINISTRADOR" -> {
                var user = userService.findAll ( );
                user.forEach ( e -> {
                    e.getInvoices ( ).forEach ( k -> {
                        clientsInvoices.put ( k, e );
                        invoiceDTOS.add ( k );
                    } );
                } );
            }
            case "CLIENTE" -> {
                btnProductos.setVisible ( false );
                btnProductos.setManaged ( false );
                btnClientes.setVisible ( false );
                btnClientes.setManaged ( false );
                HBox.setMargin ( containerLogout, new Insets ( 280, 0, 0, 0 ) );
                containerLogout.setVisible ( false );
                userService.getUserByEmail ( client.getEmail ( ) )
                        .getInvoices ( ).forEach ( e -> {
                            invoiceDTOS.add ( e );
                            clientsInvoices.put ( e, client );
                        } );
            }
            case "VENDEDOR"->{
                btnClientes.setVisible ( false );
                btnClientes.setManaged ( false );
                HBox.setMargin ( containerLogout, new Insets ( 280, 0, 0, 0 ) );
                containerLogout.setVisible ( false );
                userService.getUserByEmail ( client.getEmail ( ) )
                        .getInvoices ( ).forEach ( e -> {
                            invoiceDTOS.add ( e );
                            clientsInvoices.put ( e, client );
                        } );
            }
        }

        imgProfile.setClip ( new Circle ( Profile.IMAGE_CENTER_X.getValue ( ), Profile.IMAGE_CENTER_Y.getValue ( ),
                Profile.IMAGE_RADIUS.getValue ( ) ) );
        userName.setText ( client.getName ( ) );
        userRole.setText ( client.getRole ( ).toUpperCase ( ) );
        fillTableInvoice ( invoiceDTOS );
    }

    @FXML
    void handleMenuInicio ( ActionEvent event ) {
        switch (utilityService.getClientByRol ( ).getRole ( ).toUpperCase ( )) {
            case "ADMINISTRADOR" -> {
                stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
            }
            case "CLIENTE" -> {
                stage.setScene ( new Scene ( context.getBean ( "homeClientParent", AnchorPane.class ) ) );
            }
            case "VENDEDOR" -> {
                stage.setScene ( new Scene ( context.getBean ( "homeSellerParent", AnchorPane.class ) ) );

            }
        }
    }

    @FXML
    void handleMenuClientes ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleMenuCompra ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "purchaseParent", AnchorPane.class ) ) );
    }


    @FXML
    void handleInvoiceRegister ( ActionEvent event ) {

    }
    @FXML
    void handleLogout(){
        stage.setScene ( new Scene ( context.getBean ( "loginParent", AnchorPane.class ) ) );

    }
    @FXML
    void handleDetails ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        currentInvoice = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "invoiceDetailsParent", AnchorPane.class ) ) );
        stage.show ( );
    }

    @FXML
    void handleBefore ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && 1 <
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) - 1) + "" );

        }
        refresh ( );
    }

    @FXML
    void handleAfter ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && utilityService.totalPages ( invoiceDTOS ) >
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) + 1) + "" );
        }
        refresh ( );
    }

    @FXML
    void handlePressed ( ActionEvent event ) {

    }

    @FXML
    void handleSearch ( ActionEvent event ) {

    }

    public void refresh () {
        clientsInvoices = new HashMap<> ( );
        invoiceDTOS = new ArrayList<> ( );
        var user = userService.findAll ( );
        user.forEach ( e -> {
            e.getInvoices ( ).forEach ( k -> {
                clientsInvoices.put ( k, e );
                invoiceDTOS.add ( k );
            } );
        } );
        fillTableInvoice ( invoiceDTOS );
    }

    void fillTableInvoice ( List<InvoiceDTO> invoice ) {
        invoice = invoice.stream ( ).filter ( InvoiceDTO::isStatus ).toList ( );
        txtTotalPage.setText ( utilityService.totalPages ( invoice ) + "" );
        List<InvoiceDTO> invoiceDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), invoice );
        AtomicInteger contador = new AtomicInteger ( 0 );
        vboxContainer.getChildren ( ).forEach ( e -> {
            Circle clip = new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                    Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) );
            int count = contador.getAndIncrement ( );
            HBox container = (HBox) e;
            ImageView imageView = (ImageView) container.getChildren ( ).get ( 0 );
            imageView.setClip ( clip );
            Label clientName = (Label) container.getChildren ( ).get ( 1 );
            Label address = (Label) container.getChildren ( ).get ( 2 );
            Label zipCode = (Label) container.getChildren ( ).get ( 3 );
            Label total = (Label) container.getChildren ( ).get ( 4 );
            Button buttonDetail = (Button) container.getChildren ( ).get ( 5 );
            Button buttonEdit = (Button) container.getChildren ( ).get ( 6 );
            Button buttonRemove = (Button) container.getChildren ( ).get ( 7 );
            if (count >= invoiceDTOS.size ( )) {
                clearProductsInfo ( clientName, address, zipCode, total, buttonDetail, imageView, buttonEdit, buttonRemove );
            } else {
                InvoiceDTO invoices = invoiceDTOS.get ( count );
                var client = clientsInvoices.get ( invoices );
                imageView.setImage ( new Image ( client.getUrl ( ) ) );
                clientName.setText ( String.valueOf ( client.getName ( ) ) );
                address.setText ( invoices.getSubTotal ( ) + "" );
                zipCode.setText ( invoices.getTax ( ) + "" );
                total.setText ( String.valueOf ( invoices.getTotal ( ) ) );
                buttonEdit.setVisible ( true );
                buttonRemove.setVisible ( true );
                buttonDetail.setVisible ( true );
            }

        } );
    }

    void clearProductsInfo ( Label idLabel, Label nameLabel, Label emailLabel, Label telLabel,
                             Button buttonDetail, ImageView imageView, Button buttonEdit, Button buttonRemove ) {
        idLabel.setText ( "" );
        nameLabel.setText ( "" );
        emailLabel.setText ( "" );
        telLabel.setText ( "" );
        imageView.setImage ( null );
        buttonEdit.setVisible ( false );
        buttonRemove.setVisible ( false );
        buttonDetail.setVisible ( false );
    }

    public void handleInvoiceEdit ( ActionEvent event ) {
        if (utilityService.permsValidate ( utilityService.getClientByRol ( ).getRole ( ) )) return;
        Button button = (Button) event.getSource ( );
        currentInvoice = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        clientDTO = clientsInvoices.get ( currentInvoice );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "invoiceEditParent", GridPane.class ) ) );
        stage.show ( );
    }

    public void handleButtonRemove ( ActionEvent event ) throws JsonProcessingException {
        if (utilityService.permsValidate ( utilityService.getClientByRol ( ).getRole ( ) )) return;
        Button button = (Button) event.getSource ( );
        var invoice = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Alert alert = new Alert ( Alert.AlertType.WARNING );
        alert.setTitle ( "Confirmación" );
        alert.setHeaderText ( "¿Está seguro de eliminar el pedido?" );
        alert.setContentText ( "Si elimina el pedido, no podrá recuperar los datos." );
        Optional<ButtonType> result = alert.showAndWait ( );
        if (result.get ( ) == ButtonType.OK) {
            var client = getClientsInvoices ( ).get ( invoice );
            client.getInvoices ( ).remove ( invoice );
            invoice.setStatus ( false );
            client.getInvoices ( ).add ( invoice );
            userService.update ( client );
            fillTableInvoice ( client.getInvoices ( ) );
        }
    }

    public void handleProduct ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "productParent", AnchorPane.class ) ) );
    }
}
