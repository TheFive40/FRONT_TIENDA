package io.github.thefive40.tienda_front.controller.main;

import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.model.enums.Profile;
import io.github.thefive40.tienda_front.service.ReadImageService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HomeController implements Initializable {

    @FXML
    private HBox containerLogout;

    @FXML
    private Button searchButton;

    @FXML
    private Button btnLogout;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Button buttonNext1;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label userName;

    @FXML
    private Label userRole;

    @FXML
    private ComboBox<?> filterButton;

    private ReadImageService imageService;

    private ApplicationContext context;

    private SignUpController signUp;

    private LoginController login;

    @Autowired
    public void inject ( ReadImageService imageService, ApplicationContext context
            , LoginController login ) {
        this.imageService = imageService;
        this.context = context;
        this.login = login;
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        Circle clip = new Circle ( Profile.IMAGE_CENTER_X.getValue ( ),
                Profile.IMAGE_CENTER_Y.getValue ( ), Profile.IMAGE_RADIUS.getValue ( ) );
        signUp = context.getBean ( "SignUpController", SignUpController.class );
        if (signUp.getCurrentUser ( ) != null)
            imgProfile.setImage ( imageService.convertToImage ( signUp.getCurrentUser ( ).getImage ( ) ) );
        else
            imgProfile.setImage ( imageService.convertToImage ( login.getCurrentUser ( ).getImage ( ) ) );
        imgProfile.setClip ( clip );
    }
}
