package io.github.thefive40.tienda_front.controller.main.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ItemCartDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.model.dto.ShoppingCartDTO;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
@Component("HomeSellerController")
@Getter
public class HomeSellerController implements Initializable {
    @FXML
    private Button btnProdDest4;

    @FXML
    private Button btnProdDest3;

    @FXML
    private Button btnProdDest2;

    @FXML
    private ListView<String> cartListView;

    @FXML
    private TitledPane titledCarrito;

    @FXML
    private Label NomProd;

    @FXML
    private Button btnProd4;

    @FXML
    private Button btnProd3;

    @FXML
    private Button btnProd;

    @FXML
    private Button payButton;

    @FXML
    private Button btnLogout;

    @FXML
    private Button buttonNext2;

    @FXML
    private Button buttonNext1;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane homeParent;

    @FXML
    private HBox quantityHBox;

    @FXML
    private HBox containerLogout;

    @FXML
    private Button btnProdDest;

    @FXML
    private Button searchButton;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label precioProdDest2;

    @FXML
    private BorderPane cartBorderPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label precioProdDest4;

    @FXML
    private Label precioProdDest3;

    @FXML
    private VBox cartVBox;

    @FXML
    private Label precioProd;

    @FXML
    private Label precioProdDest;

    @FXML
    private Label precioProd4;

    @FXML
    private Label precioProd3;

    @FXML
    private Label precioProd2;

    @FXML
    private TextField quantityTextField;

    @FXML
    private ImageView imgProdDest4;

    @FXML
    private Label NomProd2;

    @FXML
    private Button btnProd2;

    @FXML
    private Label userName;

    @FXML
    private Label quantityLabel;

    @FXML
    private ImageView imgProdDest2;

    @FXML
    private ImageView imgProdDest3;

    @FXML
    private HBox cartButtonsHBox;

    @FXML
    private Label NomProdDest2;

    @FXML
    private Label NomProdDest4;

    @FXML
    private Label NomProdDest3;

    @FXML
    private ImageView imgProdDest;

    @FXML
    private ImageView imgProd4;

    @FXML
    private Button removeButton;

    @FXML
    private ImageView imgProd;

    @FXML
    private ImageView imgProd2;

    @FXML
    private ImageView imgProd3;

    @FXML
    private Label userRole;

    @FXML
    private Label NomProdDest;

    @FXML
    private Label NomProd3;

    @FXML
    private Label NomProd4;

    @FXML
    private ComboBox<String> filterButton;

    @FXML
    private Accordion cartAccordion;
    @FXML
    private VBox containerProducts;

    private ReadImageService imageService;

    private ApplicationContext context;

    private SignUpController signUp;

    private LoginController login;

    private UserService userService;

    private UtilityService<ProductDTO> utility;

    private Stage stage;

    private int contador = 1;

    private ProductService productService;

    private List<ProductDTO> products;

    @Autowired
    private ShoppingCartService cartService;

    private ClientDTO currentUser;

    private HashMap<Integer, ItemCartDTO> itemCartDTOHashMap;

    @Autowired
    public void inject ( ReadImageService imageService, ApplicationContext context
            , UtilityService<ProductDTO> utility, UserService userService ) {
        this.imageService = imageService;
        this.context = context;
        this.userService = userService;
        this.utility = utility;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        itemCartDTOHashMap = new HashMap<> ( );
        Circle clip = new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) );
        signUp = context.getBean ( "SignUpController", SignUpController.class );
        login = context.getBean ( "LoginController", LoginController.class );
        if (signUp.getCurrentUser ( ) != null) {
            imgProfile.setImage ( new Image ( signUp.getCurrentUser ( ).getUrl ( ) ) );
            userName.setText ( signUp.getCurrentUser ( ).getName ( ) );
            currentUser = signUp.getCurrentUser ( );
        } else if (login.getCurrentUser ( ) != null) {
            imgProfile.setImage ( new Image ( login.getCurrentUser ( ).getUrl ( ) ) );
            userName.setText ( login.getCurrentUser ( ).getName ( ) );
            currentUser = login.getCurrentUser ( );
        }
        try {
            ShoppingCartDTO cart = cartService.findByClient ( currentUser );
            List<String> items = new ArrayList<> ( );
            AtomicInteger integer = new AtomicInteger ( 0 );

            if (cart != null) {
                cart.getItemsCart ( ).forEach ( e -> {
                    itemCartDTOHashMap.put ( integer.get ( ), e );
                    items.add ( e.getProduct ( ).getName ( ) + " $" + e.getProduct ( ).getPrice ( ) );
                    integer.set ( integer.get ( ) + 1 );
                } );
            }
            cartListView.getItems ( ).addAll ( items );
        } catch (JsonProcessingException e) {
            throw new RuntimeException ( e );
        }
        imgProfile.setClip ( clip );
        userRole.setText ( utility.getClientByRol ().getRole ().toUpperCase () );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );
        products = productService.getProducts ( );
        utility.fillProducts ( containerProducts, products );

    }

    public void handleCart ( ActionEvent event ) throws JsonProcessingException {
        utility.addProductToCart ( (Button) event.getSource ( ) );
        ShoppingCartDTO cart = cartService.findByClient ( currentUser );
        System.out.println (cart );
        if (cart == null)
            cart = new ShoppingCartDTO ( );
        cart.setClient ( currentUser );
        var item = new ItemCartDTO ( );
        var product = utility.getProductByCart ( (Button) event.getSource ( ) );
        item.setProduct ( product );
        item.setQuantity ( 1 );
        itemCartDTOHashMap.put ( cartListView.getItems ( ).size ( ) - 1, item );
        cart.getItemsCart ( ).add ( item );
        cartService.save ( cart );

    }

    @FXML
    void handlePurchase () {
        stage.setScene ( new Scene ( context.getBean ( "purchaseParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleInvoice(){
        stage.setScene ( new Scene ( context.getBean ( "invoiceParent" , AnchorPane.class) ) );
    }
    @FXML
    void handleBtnSearch(){
        stage.setScene ( new Scene ( context.getBean ( "findProductParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleSearch(){
        stage.setScene ( new Scene ( context.getBean ( "findProductParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleItemClicked () {
        int index = cartListView.getSelectionModel ( ).getSelectedIndex ( );
        var itemCartDTO = itemCartDTOHashMap.get ( index );
        quantityTextField.setText ( itemCartDTO.getQuantity ( ) + "" );
    }

    @FXML
    void handleLogout () {
        stage.setScene ( new Scene ( context.getBean ( "loginParent", AnchorPane.class ) ) );
    }

    public void handleMenuClient ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleEstadistica () {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "statisticsParent", ScrollPane.class ) ) );
        stage.show ( );
    }

    @FXML
    void handleAddCart () throws JsonProcessingException {
        if (utility.isNumber ( quantityTextField.getText ( ) )) {
            quantityTextField.setText ( "" );
            int index = cartListView.getSelectionModel ( ).getSelectedIndex ( );
            var item = itemCartDTOHashMap.get ( index );
            int quantity = item.getQuantity ( ) + 1;
            var shoppingCartDTO = cartService.findByClient ( currentUser );
            var product = item.getProduct ( );
            var itemEntity = cartService.findByProductAndShoppingCart ( shoppingCartDTO, product );
            quantityTextField.setText ( quantity + "" );
            item.setQuantity ( quantity );
            itemEntity.setQuantity ( quantity );
            itemEntity.setShoppingCart ( shoppingCartDTO );
            itemCartDTOHashMap.put ( index, item );
            cartService.updateItemCart ( itemEntity );
        }
    }

    @FXML
    void handlePay () {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "productPurchaseParent", BorderPane.class ) ) );
        stage.setTitle ( "Compra de Productos" );
        stage.centerOnScreen ( );
        stage.show ( );
    }

    @FXML
    void handleContact () {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "contactParent", AnchorPane.class ) ) );
        stage.setTitle ( "Compra de Productos" );
        stage.centerOnScreen ( );
        stage.show ( );
    }

    @FXML
    void handleDevelopers () {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "developerParent", VBox.class ) ) );
        stage.setTitle ( "Compra de Productos" );
        stage.centerOnScreen ( );
        stage.show ( );
    }

    @FXML
    void handleConfig () {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "configParent", AnchorPane.class ) ) );
        stage.setTitle ( "Compra de Productos" );
        stage.centerOnScreen ( );
        stage.show ( );
    }

    @FXML
    void handleBefore () {
        if (contador != 1) {
            contador--;
            utility.fillProducts ( containerProducts, products );
        }
    }

    @FXML
    void handleAfter () {
        if (contador < utility.totalProductsPage ( products ))
            contador++;
        utility.fillProducts ( containerProducts, products );
    }

    @FXML
    void handleRemoveCart () {
        int index = cartListView.getSelectionModel ( ).getSelectedIndex ( );
        getCartListView ( ).getItems ( ).remove ( index );
        var item = itemCartDTOHashMap.get ( index );
        itemCartDTOHashMap.remove ( index );
        cartService.remove ( item );
        quantityTextField.setText ( "" );
    }

    @FXML
    void handleMenuProduct () {
        stage.setScene ( new Scene ( context.getBean ( "productParent", AnchorPane.class ) ) );
    }

    @Qualifier("stage")
    @Autowired
    public void setStage ( Stage stage ) {
        this.stage = stage;
    }

    @Autowired
    public void setProductService ( ProductService productService ) {
        this.productService = productService;
    }
}
