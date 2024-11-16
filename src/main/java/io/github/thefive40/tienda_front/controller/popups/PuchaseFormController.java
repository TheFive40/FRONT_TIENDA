package io.github.thefive40.tienda_front.controller.popups;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.main.HomeController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.DetailOrderDTO;
import io.github.thefive40.tienda_front.model.dto.OrderDTO;
import io.github.thefive40.tienda_front.service.ShoppingCartService;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PuchaseFormController implements Initializable {
    private final HomeController homeController;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    @FXML
    private TextField lastName;

    @FXML
    private TextField country;

    @FXML
    private TextField cvv;

    @FXML
    private TextField city;

    @FXML
    private TextField postalCode;

    @FXML
    private Label orderTotal;

    @FXML
    private TextField firstName;

    @FXML
    private TextField phone;

    @FXML
    private TextField cardholderName;

    @FXML
    private TextField shippingAddress;

    @FXML
    private TextField email;

    @FXML
    private TextField cardNumber;

    @FXML
    private TextField expirationDate;

    private ClientDTO client;
    private  OrderDTO orderDTO = new OrderDTO (  );

    public PuchaseFormController ( HomeController homeController, UserService userService, ShoppingCartService shoppingCartService ) {
        this.homeController = homeController;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        AtomicInteger totalAmount = new AtomicInteger ( );
        client = homeController.getCurrentUser ( );
        firstName.setText ( client.getName ( ) );
        lastName.setText ( client.getLastname ( ) );
        email.setText ( client.getEmail ( ) );
        phone.setText ( client.getPhone ( ) );
        homeController.getItemCartDTOHashMap ( ).forEach ( ( k, v ) -> {
            DetailOrderDTO detailOrderDTO = new DetailOrderDTO ( );
            detailOrderDTO.setProduct ( v.getProduct ( ) );
            detailOrderDTO.setAmount ( v.getQuantity ( ) );
            detailOrderDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
            orderDTO.getDetailOrder ( ).add ( detailOrderDTO );
            totalAmount.set ( (int) ((detailOrderDTO.getUnitPrice ( ) * detailOrderDTO.getAmount ( )) + totalAmount.get ( )) );
        } );
        orderTotal.setText ( totalAmount.get ( ) + "" );
    }

    @FXML
    void handlePurchase () throws JsonProcessingException {
        // TODO: Make purchase request and handle success or failure
        orderDTO.setAddress ( shippingAddress.getText ( ) );
        orderDTO.setCity ( city.getText ( ) );
        orderDTO.setCountry ( country.getText ( ) );
        orderDTO.setZipCode ( postalCode.getText ( ) );
        orderDTO.setPaymentMethod ( "Mastercard" );
        orderDTO.setDiscountCode ( "0FGLS" );
        orderDTO.setTotal ( Integer.parseInt (orderTotal.getText ()) );
        client.getOrders ( ).add ( orderDTO );
        var cart = shoppingCartService.findByClient ( client );
        client.setShoppingCart ( List.of ( cart ) );
        userService.update ( client );
    }
}
