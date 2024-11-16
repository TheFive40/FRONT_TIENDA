package io.github.thefive40.tienda_front.controller.main.menu.product;
import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.ProductService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class ProductController implements Initializable {

    @FXML
    private TextField txtPage;

    @FXML
    private TextField txtTotalPage;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Button btnAfter;

    @FXML
    private Button btnBefore;

    @FXML
    private Label userName;

    @FXML
    private VBox vboxContainer;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> filterButton;

    private SignUpController signUp;

    private LoginController login;

    private UtilityService<ProductDTO> utilityService;

    private Stage stage;

    private ApplicationContext context;

    private List<ProductDTO> products;

    private ProductService productService;

    private ProductDTO productEdit;



    @Autowired
    public void inject ( ProductService service, Stage stage, LoginController login, SignUpController signUpController
            , UtilityService<ProductDTO> utilityService, ApplicationContext context ) {
        this.stage = stage;
        this.login = login;
        this.signUp = signUpController;
        this.utilityService = utilityService;
        this.context = context;
        this.productService = service;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        products = productService.getProducts ( );
        txtTotalPage.setText ( utilityService.totalPages ( products ) + "" );
        txtPage.setText ( "1" );
        fillTableProducts ( products );
        imgProfile.setClip ( new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) ) );
        if (login.getCurrentUser ( ) != null)
            userName.setText ( login.getCurrentUser ( ).getName ( ) );
        else userName.setText ( signUp.getCurrentUser ( ).getName ( ) );
        filterButton.getItems ().addAll ( "Nombre", "Vendedor" );
        filterButton.getSelectionModel ().select ( 0 );
    }

    public void refresh () {
        List<ProductDTO> productDTOS = productService.getProducts ( );
        fillTableProducts ( productDTOS );
    }

    public void fillTableProducts ( List<ProductDTO> products ) {
        try {
            products = products.stream ( ).filter ( ProductDTO::isStatus ).toList ( );
        } catch (NullPointerException exception) {
            txtTotalPage.setText ( "1" );
            return;
        }
        txtTotalPage.setText ( utilityService.totalPages ( products ) + "" );
        List<ProductDTO> productDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), products );
        AtomicInteger contador = new AtomicInteger ( 0 );

        vboxContainer.getChildren ( ).forEach ( e -> {
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
            if (count >= productDTOS.size ( )) {
                clearProductsInfo ( idLabel, nameLabel, emailLabel, telLabel, rolLabel, imageView, buttonEdit, buttonRemove );
            } else {
                ProductDTO product = productDTOS.get ( count );

                if (product.isStatus ( )) {
                    if (product.getImg ( ) != null && !product.getImg ( ).isEmpty ( )) {
                        try {
                            imageView.setImage ( new Image ( product.getImg ( ) ) );
                        } catch (IllegalArgumentException ex) {
                            System.out.println ( "Error al cargar la imagen: " + ex.getMessage ( ) );
                            imageView.setImage ( null );
                        }
                    } else {
                        imageView.setImage ( null );
                    }
                    idLabel.setText ( String.valueOf ( product.getProductId ( ) ) );
                    nameLabel.setText ( product.getName ( ) );
                    emailLabel.setText ( product.getDescription ( ) );
                    telLabel.setText ( String.valueOf ( product.getPrice ( ) ) );
                    rolLabel.setText ( product.getClient ( ) != null ? product.getClient ( ).getName ( ) : "" ); // Verifica que `getClient()` no sea null
                    imageView.setPreserveRatio ( false );
                    imageView.setSmooth ( false );
                    buttonEdit.setVisible ( true );
                    buttonRemove.setVisible ( true );
                }
            }
        } );

    }

    @FXML
    void handlePressed ( KeyEvent keyEvent ) {
        if (keyEvent.getCode ( ).equals ( KeyCode.ENTER )) {
            List<ProductDTO> productDTOS = null;
            String item = filterButton.getSelectionModel ().getSelectedItem ();
            if (item.equalsIgnoreCase ( "Vendedor" )) {
                if (!searchTextField.getText ( ).isEmpty ()) {
                    productDTOS = productService.findProductsByClientName ( searchTextField.getText ( ) );
                    fillTableProducts ( productDTOS );
                }else{
                    refresh ();
                }
            }else if(item.equalsIgnoreCase ( "Nombre" )){
                productDTOS = productService.findByName ( searchTextField.getText () );
                fillTableProducts ( Objects.requireNonNullElseGet ( productDTOS, List::of ) );
            }

        }
    }

    @FXML
    void handleSearch () {
        String item = filterButton.getSelectionModel ().getSelectedItem ();
        if (!searchTextField.getText ( ).isEmpty ()) {
            List<ProductDTO> productos;

            if (item.equalsIgnoreCase ( "Vendedor" )){
                List<ProductDTO> productDTOS = productService.findProductsByClientName ( searchTextField.getText ( ) );
                fillTableProducts ( productDTOS );
            }else if(item.equalsIgnoreCase ( "Nombre" )){
                productos = productService.findByName ( searchTextField.getText ( ) );
                fillTableProducts ( Objects.requireNonNullElseGet ( productos, List::of ) );
            }
        }else{
            refresh ();
        }
    }
    @FXML
    void handleInvoice(){

    }
    void clearProductsInfo ( Label idLabel, Label nameLabel, Label emailLabel, Label telLabel,
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

    @FXML
    void handleBefore ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && 1 <
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) - 1) + "" );
        }
        refresh ( );
    }
    @FXML
    void handleMenuCompra(){
        stage.setScene ( new Scene ( context.getBean ( "purchaseParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleAfter ( ActionEvent event ) {
        if (utilityService.isNumber ( txtPage.getText ( ) ) && utilityService.totalPages ( products ) >
                Integer.parseInt ( txtPage.getText ( ) )) {
            txtPage.setText ( (Integer.parseInt ( txtPage.getText ( ) ) + 1) + "" );
        }
        refresh ( );
    }

    public void handleProductEdit ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        productEdit = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "productEditParent", GridPane.class ) ) );
        stage.show ( );
    }

    public void handleButtonRemove ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        ProductDTO productRemove = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        productRemove.setStatus ( false );
        productService.save ( productRemove );
        refresh ( );
    }

    public void handleMenuInicio ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
    }

    @FXML
    void handleMenuClientes () {
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );
    }

    public void handleProductRegister ( ActionEvent event ) {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "productRegisterParent", AnchorPane.class ) ) );
        stage.show ( );

    }
}
