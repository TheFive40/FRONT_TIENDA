package io.github.thefive40.tienda_front.controller.main.menu.purchase;

import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.OrderDTO;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PurchaseController implements Initializable {
    private final UtilityService<OrderDTO> utilityService;
    private final UserService userService;
    @FXML
    private TextField txtTotalPage;

    @FXML
    private VBox vboxContainer;
    @FXML
    private TextField txtPage;

    private HashMap<OrderDTO, ClientDTO> clientOrders = new HashMap<> (  );

    public PurchaseController ( UtilityService<OrderDTO> utilityService, UserService userService ) {
        this.utilityService = utilityService;
        this.userService = userService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        txtPage.setText ( "1" );
        List<OrderDTO> orderDTOS = new ArrayList<> ( );
        userService.findAll ( ).forEach ( e -> {
            orderDTOS.addAll ( e.getOrders ( ) );
            e.getOrders ().forEach ( v->{
                clientOrders.put ( v,e );
            });
        } );
        fillTablePurchase ( orderDTOS );
    }

    void fillTablePurchase ( List<OrderDTO> orders ) {
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
            Button buttonDetail = (Button) container.getChildren ().get ( 5 );
            Button buttonEdit = (Button) container.getChildren ( ).get ( 6 );
            Button buttonRemove = (Button) container.getChildren ( ).get ( 7 );
            if (count >= ordersDTOS.size ( )) {
                clearProductsInfo ( clientName, address, zipCode, total, buttonDetail,imageView, buttonEdit, buttonRemove );
            } else {
                OrderDTO order = ordersDTOS.get ( count );
                var client = clientOrders.get ( order );
                imageView.setImage ( new Image ( client.getUrl ( ) ) );
                clientName.setText ( String.valueOf (client.getName ( ) ) );
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

    }

    @FXML
    void handleMenuClientes ( ActionEvent event ) {

    }

    @FXML
    void handleDetails ( ActionEvent event ) {

    }

    @FXML
    void handleProductEdit ( ActionEvent event ) {

    }

    @FXML
    void handleButtonRemove ( ActionEvent event ) {

    }

    public void handlePressed ( KeyEvent keyEvent ) {
    }

    public void handleSearch ( ActionEvent event ) {
    }

    public void handleProductRegister ( ActionEvent event ) {
    }

    public void handleAfter ( ActionEvent event ) {
    }

    public void handleBefore ( ActionEvent event ) {
    }
}
