package io.github.thefive40.tienda_front.controller.main.menu.product.popups;

import io.github.thefive40.tienda_front.controller.main.menu.product.ProductController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import io.github.thefive40.tienda_front.notifications.information.AlertRegister;
import io.github.thefive40.tienda_front.service.ProductService;
import io.github.thefive40.tienda_front.service.UserService;
import io.github.thefive40.tienda_front.service.UtilityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ProductEditController implements Initializable {
    @FXML
    private TextField descripcionField;

    @FXML
    private Button actualizarButton;

    @FXML
    private GridPane mainGrid;

    @FXML
    private TextField imagenField;

    @FXML
    private ComboBox<String> estadoComboBox;

    @FXML
    private TextField precioField;

    @FXML
    private TextField vendedorField;

    @FXML
    private TextField nombreField;

    private ProductDTO productDTO;

    private ProductController productController;

    private ProductService productService;

    private UtilityService<ProductDTO> utilityService;
    private UserService userService;

    @Autowired
    void inject ( ProductController productController, UtilityService<ProductDTO> utilityService, ProductService productService ) {
        this.productController = productController;
        this.utilityService = utilityService;
        this.productService = productService;
    }


    @FXML
    void handleUpdate ( ActionEvent event ) {
        productDTO.setName ( nombreField.getText ( ) );
        productDTO.setDescription ( descripcionField.getText ( ) );
        productDTO.setImg ( imagenField.getText ( ) );
        productDTO.setPrice ( Double.valueOf ( precioField.getText ( ) ) );
        ClientDTO clientDTO =  userService.getUserByEmail ( vendedorField.getText ( ) );
        productDTO.setClient ( clientDTO );
        productDTO.setStatus ( estadoComboBox.getValue ( ).equals ( "Activo" ) );
        productService.save ( productDTO );
        productController.refresh ();
        new AlertRegister ().showUpdateProduct ();
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        productDTO = productController.getProductEdit ( );
        nombreField.setText ( productDTO.getName ( ) );
        descripcionField.setText ( productDTO.getDescription ( ) );
        imagenField.setText ( productDTO.getImg ( ) );
        precioField.setText ( String.valueOf ( productDTO.getPrice ( ) ) );
        vendedorField.setText ( productDTO.getClient ( ).getEmail ( ) );
        estadoComboBox.setValue ( productDTO.isStatus ( ) ? "Activo" : "Inactivo" );
    }

    @FXML
    void handleImg () {
        utilityService.loadImage ( imagenField );
    }


    @Autowired
    public void setUserService ( UserService userService ) {
        this.userService = userService;
    }
}
