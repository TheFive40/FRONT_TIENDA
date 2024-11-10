package io.github.thefive40.tienda_front.controller.main.menu.product.popups;

import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.controller.auth.SignUpController;
import io.github.thefive40.tienda_front.controller.main.menu.product.ProductController;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.config.notifications.information.AlertRegister;
import io.github.thefive40.tienda_front.service.ProductService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class RegisterProductController implements Initializable {

    private final UtilityService<ProductDTO> utilityService;
    private final ProductService productService;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtImage;

    @FXML
    private TextField txtDesc;

    @FXML
    private TextField txtPrice;

    private LoginController login;

    private SignUpController signUpController;

    private ProductController productController;

    public RegisterProductController ( UtilityService<ProductDTO> utilityService, LoginController login
            , SignUpController signUpController, ProductService productService, ProductController product ) {
        this.login = login;
        this.signUpController = signUpController;
        this.utilityService = utilityService;
        this.productService = productService;
        this.productController = product;
    }

    @FXML
    void handleRegisterProduct ( ActionEvent event ) {
        ProductDTO productDTO = new ProductDTO ( );
        productDTO.setImg ( txtImage.getText ( ) );
        productDTO.setName ( txtName.getText ( ) );
        if (utilityService.isNumber ( txtPrice.getText ( ) )) {
            productDTO.setPrice ( Double.parseDouble ( txtPrice.getText ( ) ) );
        } else {
            productDTO.setPrice ( 0.0 );
        }
        productDTO.setDescription ( txtDesc.getText ( ) );
        productDTO.setClient ( (login.getCurrentUser ( ) != null) ? login.getCurrentUser ( ) : signUpController.getCurrentUser ( ) );
        productService.save ( productDTO );
        new AlertRegister ( ).showProductRegister ( );
        productController.refresh ();
    }

    @FXML
    void handleImage () {
        utilityService.loadImage ( txtImage );
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {

    }
}
