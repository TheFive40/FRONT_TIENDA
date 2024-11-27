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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * HomeController manages the main home view for administrative users in the application.
 * It provides functionality for product browsing, shopping cart operations, and navigation to other views.
 */
@Component("HomeController")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
public class HomeController implements Initializable {
    // FXML Elements
    @FXML private ListView<String> cartListView;
    @FXML private AnchorPane homeParent;
    @FXML private VBox containerProducts;
    @FXML private Label userName;
    @FXML private Label userRole;
    @FXML private ImageView imgProfile;
    @FXML private Accordion cartAccordion;
    @FXML private TitledPane titledCarrito;
    @FXML private TextField searchTextField;
    @FXML private TextField quantityTextField;

    // Services and dependencies
    private ReadImageService imageService;
    private ApplicationContext context;
    private SignUpController signUp;
    private LoginController login;
    private UserService userService;
    private UtilityService<ProductDTO> utility;
    private ProductService productService;
    private ShoppingCartService cartService;

    // State management
    private Stage stage;
    private ClientDTO currentUser;
    private int contador = 1;
    private List<ProductDTO> products;
    private HashMap<Integer, ItemCartDTO> itemCartDTOHashMap;
    /**
     * Injects required dependencies into the controller.
     *
     * @param imageService the {@link ReadImageService} for image handling.
     * @param context the {@link ApplicationContext} for accessing Spring beans.
     * @param utility the {@link UtilityService} for product-related operations.
     * @param userService the {@link UserService} for user operations.
     */
    @Autowired
    public void inject ( ReadImageService imageService, ApplicationContext context
            , UtilityService utility, UserService userService ) {
        this.imageService = imageService;
        this.context = context;
        this.userService = userService;
        this.utility = utility;
    }
    /**
     * Initializes the home view.
     * Configures user-specific settings, shopping cart, and product display.
     *
     * @param url the location used to resolve relative paths.
     * @param resourceBundle the resources used to localize the root object.
     */
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
            //currentUser = signUp.getCurrentUser ( );
            currentUser = userService.getUserByEmail ( signUp.getCurrentUser ().getEmail () );
        } else if (login.getCurrentUser ( ) != null) {
            imgProfile.setImage ( new Image ( login.getCurrentUser ( ).getUrl ( ) ) );
            userName.setText ( login.getCurrentUser ( ).getName ( ) );
            //currentUser = login.getCurrentUser ( );
            currentUser = userService.getUserByEmail ( login.getCurrentUser ().getEmail () );

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
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );
        products = productService.getProducts ( );
        utility.fillProducts ( containerProducts, products );

    }
    /**
     * Handles adding a product to the user's shopping cart.
     *
     * @param event the action event triggered by the add-to-cart button.
     * @throws JsonProcessingException if an error occurs during cart data processing.
     */
    public void handleCart ( ActionEvent event ) throws JsonProcessingException {
        utility.addProductToCart ( (Button) event.getSource ( ) );
        System.out.println (currentUser );
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
    /**
     * Handles adding an item to the cart.
     *
     * @throws JsonProcessingException if an error occurs during cart update.
     */
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
    /**
     * Handles navigation to the previous page of products.
     * <p>
     * Decrements the page counter and updates the product display in the container
     * if the current page is not the first one.
     */
    @FXML
    void handleBefore () {
        if (contador != 1) {
            contador--;
            utility.fillProducts ( containerProducts, products );
        }
    }
    /**
     * Handles navigation to the next page of products.
     * <p>
     * Increments the page counter and updates the product display in the container
     * if the current page is not the last one.
     */
    @FXML
    void handleAfter () {
        if (contador < utility.totalProductsPage ( products ))
            contador++;
        utility.fillProducts ( containerProducts, products );
    }
    /**
     * Removes an item from the user's shopping cart.
     */
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
