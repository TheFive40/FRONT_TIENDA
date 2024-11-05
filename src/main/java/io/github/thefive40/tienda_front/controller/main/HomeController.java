package io.github.thefive40.tienda_front.controller.main;

import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.ReadImageService;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
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
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
public class HomeController implements Initializable {
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
    private ComboBox<?> filterButton;

    @FXML
    private Accordion cartAccordion;

    private ReadImageService imageService;

    private ApplicationContext context;

    private SignUpController signUp;

    private LoginController login;

    private UserService userService;

    private UtilityService utility;
    private Stage stage;


    @Autowired
    public void inject ( ReadImageService imageService, ApplicationContext context
            , UtilityService utility, UserService userService ) {
        this.imageService = imageService;
        this.context = context;
        this.userService = userService;
        this.utility = utility;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        Circle clip = new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) );
        signUp = context.getBean ( "SignUpController", SignUpController.class );
        login = context.getBean ( "LoginController", LoginController.class );
        if (signUp.getCurrentUser ( ) != null) {
            imgProfile.setImage ( new Image ( signUp.getCurrentUser ().getUrl () )  );
            userName.setText ( signUp.getCurrentUser ().getName () );
        } else if(login.getCurrentUser () != null){
            imgProfile.setImage ( new Image ( login.getCurrentUser ().getUrl () ) );
            userName.setText ( login.getCurrentUser ( ).getName ( ) );
        }
        imgProfile.setClip ( clip );
        titledCarrito.setExpanded ( true );
        cartAccordion.setExpandedPane ( titledCarrito );

    }

    public void handleCart ( ActionEvent event ) {
        utility.addProductToCart ( (Button) event.getSource () );

    }

    public void handleClient ( ActionEvent event ) {
        stage.setScene ( new Scene ( context.getBean ( "clientParent", AnchorPane.class ) ) );
    }

    @Qualifier("stage")
    @Autowired
    public void setStage ( Stage stage ) {
        this.stage = stage;
    }
}
