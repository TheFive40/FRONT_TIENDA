package io.github.thefive40.tienda_front.controller.popups;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.main.home.HomeClientController;
import io.github.thefive40.tienda_front.controller.main.home.HomeController;
import io.github.thefive40.tienda_front.controller.main.home.HomeSellerController;
import io.github.thefive40.tienda_front.model.dto.*;
import io.github.thefive40.tienda_front.service.ShoppingCartService;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PuchaseFormController implements Initializable {
    private final HomeController homeController;
    private final HomeClientController clientController;
    private final HomeSellerController sellerController;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final UtilityService<OrderDTO> utilityService;
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

    private OrderDTO orderDTO;

    private InvoiceDTO invoiceDTO;

    public PuchaseFormController ( HomeController homeController, HomeClientController clientController,
                                   HomeSellerController sellerController,UserService userService, ShoppingCartService shoppingCartService, UtilityService<OrderDTO> utilityService ) {
        this.homeController = homeController;
        this.clientController = clientController;
        this.sellerController = sellerController;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.utilityService = utilityService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        orderDTO = new OrderDTO ( );
        invoiceDTO = new InvoiceDTO ( );
        AtomicInteger totalAmount = new AtomicInteger ( );
        //client = homeController.getCurrentUser ( );
        client = utilityService.getClientByRol ( );
        firstName.setText ( client.getName ( ) );
        lastName.setText ( client.getLastname ( ) );
        email.setText ( client.getEmail ( ) );
        phone.setText ( client.getPhone ( ) );
        if (!homeController.getItemCartDTOHashMap ().isEmpty ()){
            homeController.getItemCartDTOHashMap ( ).forEach ( ( k, v ) -> {
                DetailInvoiceDTO detailInvoiceDTO = new DetailInvoiceDTO ( );
                DetailOrderDTO detailOrderDTO = new DetailOrderDTO ( );
                detailOrderDTO.setProduct ( v.getProduct ( ) );
                detailOrderDTO.setAmount ( v.getQuantity ( ) );
                detailOrderDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                orderDTO.getDetailOrder ( ).add ( detailOrderDTO );
                totalAmount.set ( (int) ((detailOrderDTO.getUnitPrice ( ) * detailOrderDTO.getAmount ( )) + totalAmount.get ( )) );
                detailInvoiceDTO.setQuantity ( v.getQuantity ( ) );
                detailInvoiceDTO.setProduct ( v.getProduct ( ) );
                detailInvoiceDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                invoiceDTO.getDetailsInvoice ( ).add ( detailInvoiceDTO );
            } );
        }else if(!sellerController.getItemCartDTOHashMap ().isEmpty ()){
            sellerController.getItemCartDTOHashMap ( ).forEach ( ( k, v ) -> {
                DetailInvoiceDTO detailInvoiceDTO = new DetailInvoiceDTO ( );
                DetailOrderDTO detailOrderDTO = new DetailOrderDTO ( );
                detailOrderDTO.setProduct ( v.getProduct ( ) );
                detailOrderDTO.setAmount ( v.getQuantity ( ) );
                detailOrderDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                orderDTO.getDetailOrder ( ).add ( detailOrderDTO );
                totalAmount.set ( (int) ((detailOrderDTO.getUnitPrice ( ) * detailOrderDTO.getAmount ( )) + totalAmount.get ( )) );
                detailInvoiceDTO.setQuantity ( v.getQuantity ( ) );
                detailInvoiceDTO.setProduct ( v.getProduct ( ) );
                detailInvoiceDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                invoiceDTO.getDetailsInvoice ( ).add ( detailInvoiceDTO );
            } );
        }else{
            clientController.getItemCartDTOHashMap ( ).forEach ( ( k, v ) -> {
                DetailInvoiceDTO detailInvoiceDTO = new DetailInvoiceDTO ( );
                DetailOrderDTO detailOrderDTO = new DetailOrderDTO ( );
                detailOrderDTO.setProduct ( v.getProduct ( ) );
                detailOrderDTO.setAmount ( v.getQuantity ( ) );
                detailOrderDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                orderDTO.getDetailOrder ( ).add ( detailOrderDTO );
                totalAmount.set ( (int) ((detailOrderDTO.getUnitPrice ( ) * detailOrderDTO.getAmount ( )) + totalAmount.get ( )) );
                detailInvoiceDTO.setQuantity ( v.getQuantity ( ) );
                detailInvoiceDTO.setProduct ( v.getProduct ( ) );
                detailInvoiceDTO.setUnitPrice ( v.getProduct ( ).getPrice ( ) );
                invoiceDTO.getDetailsInvoice ( ).add ( detailInvoiceDTO );
            } );
        }

        invoiceDTO.setTax ( 0.19 );
        invoiceDTO.setSubTotal ( totalAmount.get ( ) );
        invoiceDTO.setStartDate ( new Date ( ) );
        invoiceDTO.setDiscount ( 0.0 );
        var total = totalAmount.get ( ) * invoiceDTO.getTax ( ) + totalAmount.get ( );
        invoiceDTO.setTotal ( total );
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
        orderDTO.setTotal ( Integer.parseInt ( orderTotal.getText ( ) ) );
        client.getOrders ( ).add ( orderDTO );
        var cart = shoppingCartService.findByClient ( client );
        client.setShoppingCart ( List.of ( cart ) );
        client.getInvoices ( ).add ( invoiceDTO );
        userService.update ( client );
    }
}
