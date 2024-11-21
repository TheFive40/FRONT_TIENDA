package io.github.thefive40.tienda_front.controller.main.menu.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.DetailOrderDTO;
import io.github.thefive40.tienda_front.model.dto.OrderDTO;
import io.github.thefive40.tienda_front.service.OrderService;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component("PurchaseController")
@Getter
public class PurchaseController implements Initializable {
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> filterButton;

    private final UtilityService<OrderDTO> utilityService;

    private final UserService userService;
    @FXML
    private TextField txtTotalPage;

    @FXML
    private VBox vboxContainer;
    @FXML
    private TextField txtPage;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnClientes;
    @FXML
    private HBox containerLogout;
    @FXML
    private AnchorPane detailsPurchase;
    private HashMap<OrderDTO, ClientDTO> clientOrders;

    @Autowired
    private OrderService orderService;

    private List<OrderDTO> orderDTOS;

    private List<DetailOrderDTO> clientDetailOrders;

    private OrderDTO currentOrder;

    @Autowired
    private ApplicationContext context;

    private ClientDTO clientOrder;
    @Qualifier("stage")
    @Autowired
    private Stage stage;


    public PurchaseController ( UtilityService<OrderDTO> utilityService, UserService userService ) {
        this.utilityService = utilityService;
        this.userService = userService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        clientOrders = new HashMap<> ( );
        var currentUser = utilityService.getClientByRol ( );
        txtPage.setText ( "1" );
        orderDTOS = new ArrayList<> ( );
        switch (currentUser.getRole ( ).toUpperCase ( )) {
            case "ADMINISTRADOR" -> {
                userService.findAll ( ).forEach ( e -> {
                    orderDTOS.addAll ( e.getOrders ( ) );
                    e.getOrders ( ).forEach ( v -> {
                        clientOrders.put ( v, e );
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
                userService.getUserByEmail ( currentUser.getEmail ( ) )
                        .getOrders ( ).forEach ( e -> {
                            orderDTOS.add ( e );
                            clientOrders.put ( e, currentUser );
                        } );
            }
            case "VENDEDOR"->{
                btnClientes.setVisible ( false );
                btnClientes.setManaged ( false );
                HBox.setMargin ( containerLogout, new Insets ( 280, 0, 0, 0 ) );
                containerLogout.setVisible ( false );
                userService.getUserByEmail ( currentUser.getEmail ( ) )
                        .getOrders ( ).forEach ( e -> {
                            orderDTOS.add ( e );
                            clientOrders.put ( e, currentUser );
                        } );
            }
        }
        filterButton.getItems ( ).addAll ( "Ciudad" );
        filterButton.getSelectionModel ( ).select ( 0 );

        fillTablePurchase ( orderDTOS );

    }

    void fillTablePurchase ( List<OrderDTO> orders ) {
        orders = orders.stream ( ).filter ( OrderDTO::isStatus ).toList ( );
        txtTotalPage.setText ( utilityService.totalPages ( orders ) + "" );
        List<OrderDTO> ordersDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), orders );
        AtomicInteger contador = new AtomicInteger ( 0 );
        vboxContainer.getChildren ( ).forEach ( e -> {
            int count = contador.getAndIncrement ( );
            HBox container = (HBox) e;
            ImageView imageView = (ImageView) container.getChildren ( ).get ( 0 );
            Label clientName = (Label) container.getChildren ( ).get ( 1 );
            Label address = (Label) container.getChildren ( ).get ( 2 );
            Label zipCode = (Label) container.getChildren ( ).get ( 3 );
            Label total = (Label) container.getChildren ( ).get ( 4 );
            Button buttonDetail = (Button) container.getChildren ( ).get ( 5 );
            Button buttonEdit = (Button) container.getChildren ( ).get ( 6 );
            Button buttonRemove = (Button) container.getChildren ( ).get ( 7 );
            if (count >= ordersDTOS.size ( )) {
                clearProductsInfo ( clientName, address, zipCode, total, buttonDetail, imageView, buttonEdit, buttonRemove );
            } else {
                OrderDTO order = ordersDTOS.get ( count );
                var client = clientOrders.get ( order );
                imageView.setImage ( new Image ( client.getUrl ( ) ) );
                clientName.setText ( String.valueOf ( client.getName ( ) ) );
                address.setText ( order.getAddress ( ) );
                zipCode.setText ( order.getZipCode ( ) );
                total.setText ( String.valueOf ( order.getTotal ( ) ) );
                imageView.setPreserveRatio ( false );
                imageView.setSmooth ( false );
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
    void handleMenuProductos () {
        stage.setScene ( new Scene ( context.getBean ( "productParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleMenuClientes ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );

    }

    @FXML
    void handleDetails ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        OrderDTO ob;
        ob = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        clientDetailOrders = ob.getDetailOrder ( );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "detailsPurchaseParent", AnchorPane.class ) ) );
        stage.show ( );
    }

    @FXML
    void handlePurchaseEdit ( ActionEvent event ) {
        String rol = utilityService.getClientByRol ().getRole ();

        if(utilityService.permsValidate ( rol )) return;
        Button button = (Button) event.getSource ( );
        currentOrder = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        clientOrder = clientOrders.get ( currentOrder );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "purchaseEditParent", GridPane.class ) ) );
        stage.show ( );
    }

    @FXML
    void handleButtonRemove ( ActionEvent event ) throws JsonProcessingException {
        String rol = utilityService.getClientByRol ().getRole ();
        if(utilityService.permsValidate ( rol )) return;
        Button button = (Button) event.getSource ( );
        var order = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Alert alert = new Alert ( Alert.AlertType.WARNING );
        alert.setTitle ( "Confirmación" );
        alert.setHeaderText ( "¿Está seguro de eliminar el pedido?" );
        alert.setContentText ( "Si elimina el pedido, no podrá recuperar los datos." );
        Optional<ButtonType> result = alert.showAndWait ( );
        if (result.get ( ) == ButtonType.OK) {
            order.setStatus ( false );
            var client = clientOrders.get ( order );
            orderDTOS.remove ( order );
            userService.update ( client );
            fillTablePurchase ( orderDTOS );
        }
    }

    @FXML
    void handleInvoice () {

        stage.setScene ( new Scene ( context.getBean ( "invoiceParent", AnchorPane.class ) ) );
    }

    public void handlePressed ( KeyEvent keyEvent ) {
        if (keyEvent.getCode ( ).equals ( KeyCode.ENTER )) {
            if (!searchTextField.getText ( ).isEmpty ( )) {
                List<OrderDTO> clientDTOS;
                String item = filterButton.getSelectionModel ( ).getSelectedItem ( );
                if (item.equalsIgnoreCase ( "Ciudad" )) {
                    clientDTOS = orderService.findByCity ( searchTextField.getText ( ) );
                    fillTablePurchase ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
                }

            } else {
                fillTablePurchase ( orderDTOS );
            }
        }
    }

    public void handleSearch () {
        if (!searchTextField.getText ( ).isEmpty ( )) {
            List<OrderDTO> clientDTOS;
            String item = filterButton.getSelectionModel ( ).getSelectedItem ( );
            if (item.equalsIgnoreCase ( "Ciudad" )) {
                clientDTOS = orderService.findByCity ( searchTextField.getText ( ) );
                fillTablePurchase ( Objects.requireNonNullElseGet ( clientDTOS, List::of ) );
            }

        } else {
            fillTablePurchase ( orderDTOS );
        }
    }

    public void handleProductRegister ( ActionEvent event ) {
    }

    public void handleAfter ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && utilityService.totalPages ( orderDTOS ) >
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) + 1) + "" );
        }
        refresh ( );
    }

    public void refresh () {
        clientOrders = new HashMap<> ( );
        orderDTOS = new ArrayList<> ( );
        userService.findAll ( ).forEach ( e -> {
            orderDTOS.addAll ( e.getOrders ( ) );
            e.getOrders ( ).forEach ( v -> {
                clientOrders.put ( v, e );
            } );
        } );
        fillTablePurchase ( orderDTOS );
    }

    public void handleBefore ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && 1 <
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) - 1) + "" );
        }
        refresh ( );
    }
}
