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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
    }

    public void refresh () {
        fillTableProducts ( productService.getProducts ( ) );
    }

    public void fillTableProducts ( List<ProductDTO> products ) {
        List<ProductDTO> clientDTOS = utilityService.getItemsByPage ( Integer.parseInt ( txtPage.getText ( ) ), products );
        Stream<ProductDTO> clientDTOStream = clientDTOS.stream ( ).filter ( ProductDTO::isStatus );
        clientDTOS = clientDTOStream.toList ( );
        List<ProductDTO> finalProductDTO = clientDTOS;
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
            if (count >= finalProductDTO.size ( )) {
                clearProductsInfo ( idLabel, nameLabel, emailLabel, telLabel,
                        rolLabel, imageView, buttonEdit, buttonRemove );
            } else {
                ProductDTO product = finalProductDTO.get ( count );
                if (product.isStatus ( )) {
                    imageView.setImage ( new Image ( product.getImg ( ) ) );
                    idLabel.setText ( String.valueOf ( product.getProductId ( ) ) );
                    nameLabel.setText ( product.getName ( ) );
                    emailLabel.setText ( product.getDescription ( ) );
                    telLabel.setText ( product.getPrice ( ) + "" );
                    rolLabel.setText ( product.getClient ( ).getName ( ) );
                    imageView.setPreserveRatio ( false );
                    imageView.setSmooth ( false );
                    buttonEdit.setVisible ( true );
                    buttonRemove.setVisible ( true );
                }
            }
        } );
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

    }

    @FXML
    void handleAfter ( ActionEvent event ) {

    }

    public void handleProductEdit ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        productEdit = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        Stage stage = new Stage (  );
        stage.setScene ( new Scene ( context.getBean ( "productEditParent", GridPane.class ) ) );
        stage.show ();
    }

    public void handleButtonRemove ( ActionEvent event ) {
        Button button = (Button) event.getSource ( );
        productEdit = utilityService.findItemDto ( button, Integer.parseInt ( txtPage.getText ( ) ) );
        productEdit.setStatus ( false );
        productService.save ( productEdit );
        refresh ( );
    }

    public void handleMenuInicio ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "homeParent", AnchorPane.class ) ) );
    }
    @FXML
    void handleMenuClientes(){
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );
    }
    public void handleProductRegister ( ActionEvent event ) {
        Stage stage = new Stage ( );
        stage.setScene ( new Scene ( context.getBean ( "productRegisterParent", AnchorPane.class ) ) );
        stage.show ( );

    }
}
