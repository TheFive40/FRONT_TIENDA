package io.github.thefive40.tienda_front.controller.main.home;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.service.ProductService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Getter
public class FindProductController implements Initializable {

    @FXML
    private VBox containerProducts;

    @FXML
    private ListView<String> cartListView;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TitledPane titledCarrito;

    @FXML
    private Accordion cartAccordion;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnClientes;

    @FXML
    private HBox containerLogout;

    @Qualifier("stage")
    @Autowired
    private Stage stage;

    private final ProductService productService;

    private HomeController homeController;

    private HomeClientController homeClientController;

    private HomeSellerController homeSellerController;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UtilityService utilityService;


    public FindProductController ( ProductService productService ) {
        this.productService = productService;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        switch (utilityService.getClientByRol ( ).getRole ( ).toUpperCase ( )) {
            case "ADMINISTRADOR" -> {
                findByAdmin ( );
            }
            case "CLIENTE" -> {
                btnProductos.setVisible ( false );
                btnProductos.setManaged ( false );
                btnClientes.setVisible ( false );
                btnClientes.setManaged ( false );
                HBox.setMargin ( containerLogout, new Insets ( 280, 0, 0, 0 ) );
                containerLogout.setVisible ( false );
                findByClient ( );
            }
            case "VENDEDOR" -> {
                btnClientes.setVisible ( false );
                btnClientes.setManaged ( false );
                containerLogout.setVisible ( false );
                findBySeller ();
            }
        }
    }
    @FXML
    void handleInvoice(){

    }
    protected void findByAdmin () {
        homeController = context.getBean ( "HomeController", HomeController.class );
        String searchText = homeController.getSearchTextField ( ).getText ( );
        List<ProductDTO> productDTOS = productService.getProducts ( );
        List<ProductDTO> products = productDTOS.stream ( ).
                filter ( e -> e.getName ( ).toUpperCase ( ).contains ( searchText.toUpperCase ( ) ) ).toList ( );
        cartListView.itemsProperty ( ).bindBidirectional ( homeController.getCartListView ( ).itemsProperty ( ) );
        cartListView.selectionModelProperty ( ).bindBidirectional ( homeController.getCartListView ( ).selectionModelProperty ( ) );
        quantityTextField.textProperty ( ).bindBidirectional ( homeController.getQuantityTextField ( ).textProperty ( ) );
        homeController.getUtility ( ).fillProducts ( containerProducts, products );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );
    }

    protected void findByClient () {
        homeClientController = context.getBean ( "HomeClientController", HomeClientController.class );
        String searchText = homeClientController.getSearchTextField ( ).getText ( );
        List<ProductDTO> productDTOS = productService.getProducts ( );
        List<ProductDTO> products = productDTOS.stream ( ).
                filter ( e -> e.getName ( ).toUpperCase ( ).contains ( searchText.toUpperCase ( ) ) ).toList ( );
        getCartListView ( ).itemsProperty ( ).bindBidirectional ( homeClientController.getCartListView ( ).itemsProperty ( ) );
        getCartListView ( ).selectionModelProperty ( ).bindBidirectional ( homeClientController.getCartListView ( ).selectionModelProperty ( ) );
        getQuantityTextField ( ).textProperty ( ).bindBidirectional ( homeClientController.getQuantityTextField ( ).textProperty ( ) );
        homeClientController.getUtility ( ).fillProducts ( containerProducts, products );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );
    }

    protected void findBySeller () {
        homeSellerController = context.getBean ( "HomeSellerController", HomeSellerController.class );
        String searchText = homeSellerController.getSearchTextField ( ).getText ( );
        List<ProductDTO> productDTOS = productService.getProducts ( );
        List<ProductDTO> products = productDTOS.stream ( ).
                filter ( e -> e.getName ( ).toUpperCase ( ).contains ( searchText.toUpperCase ( ) ) ).toList ( );
        getCartListView ( ).itemsProperty ( ).bindBidirectional ( homeSellerController.getCartListView ( ).itemsProperty ( ) );
        getCartListView ( ).selectionModelProperty ( ).bindBidirectional ( homeSellerController.getCartListView ( ).selectionModelProperty ( ) );
        getQuantityTextField ( ).textProperty ( ).bindBidirectional ( homeSellerController.getQuantityTextField ( ).textProperty ( ) );
        homeSellerController.getUtility ( ).fillProducts ( containerProducts, products );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );
    }

    public void handleEstadistica ( ActionEvent event ) {
        homeController.handleEstadistica ( );
    }

    public void handleConfig ( ActionEvent event ) {
        homeController.handleConfig ( );
    }

    public void handleInicio () {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            stage.setScene ( new Scene ( context.getBean ( "homeClientParent", AnchorPane.class ) ) );
        else
            stage.setScene ( new Scene ( context.getBean ( "homeSellerParent", AnchorPane.class ) ) );
    }

    public void handleDevelopers ( ActionEvent event ) {
        homeController.handleDevelopers ( );
    }

    public void handleContact ( ActionEvent event ) {
        homeController.handleContact ( );
    }

    public void handleMenuProduct ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleMenuProduct (  );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "VENDEDOR" ))
            homeClientController.handleMenuProduct ();
        else
            homeSellerController.handleMenuProduct ();
    }

    public void handlePurchase ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handlePurchase (  );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handlePurchase ();
        else
            homeSellerController.handlePurchase ( );
    }

    public void handleCart ( ActionEvent event ) throws JsonProcessingException {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleCart ( event );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleCart ( event );
        else
            homeSellerController.handleCart ( event );
    }

    public void handlePay ( ActionEvent event ) throws JsonProcessingException {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handlePay ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handlePay ( );
        else
            homeSellerController.handlePay ();
    }

    public void handleItemClicked ( MouseEvent mouseEvent ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleItemClicked ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleItemClicked ( );
        else
            homeSellerController.handleItemClicked ();
    }

    public void handleAddCart ( ActionEvent event ) throws JsonProcessingException {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleAddCart ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleAddCart ( );
        else
            homeSellerController.handleAddCart ();
    }

    public void handleRemoveCart ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleRemoveCart ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleRemoveCart ( );
        else
            homeSellerController.handleRemoveCart ();
    }

    public void handleAfter ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleAfter ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleAfter ( );
        else
            homeSellerController.handleAfter ();
    }

    public void handleBefore ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleBefore ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleBefore ( );
        else
            homeSellerController.handleBefore ();
    }

    public void handleMenuClient ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleMenuClient (event);
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleMenuClient (event );
        else
            homeSellerController.handleMenuClient ( event );
    }

    public void handleLogout ( ActionEvent event ) {
        if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "ADMINISTRADOR" ))
            homeController.handleLogout ( );
        else if (utilityService.getClientByRol ( ).getRole ( ).equalsIgnoreCase ( "CLIENTE" ))
            homeClientController.handleLogout ( );
        else
            homeSellerController.handleLogout ();
    }
}
