package io.github.thefive40.tienda_front.service;
import io.github.thefive40.tienda_front.controller.main.home.HomeClientController;
import io.github.thefive40.tienda_front.controller.main.home.HomeController;
import io.github.thefive40.tienda_front.controller.main.home.HomeSellerController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.model.dto.ProductDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Getter
@Scope("prototype")
public class UtilityService<T> {

    private List<T> items;

    @Setter
    private static int ITEMS_PER_PAGE = 5;

    private static int PRODUCT_PER_PAGE = 8;

    private Logger logger;

    private ProductService productService;

    @Autowired
    private ApplicationContext context;


    public boolean isNumber ( String text ) {
        try {
            Double.parseDouble ( text );
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public T findItemDto ( Button button, int page ) {
        List<T> clientDTOS = getItemsByPage ( page, items );
        String num = button.getId ( ).charAt ( button.getId ( ).length ( ) - 1 ) + "";
        T clientDTO = null;
        if (isNumber ( num )) {
            int userIndex = Integer.parseInt ( num ) - 1;
            clientDTO = clientDTOS.get ( userIndex );
        }
        return clientDTO;
    }

    public void addProductToCart ( Button button ) {
        String rol = getClientByRol ().getRole ();
        String price = "";
        String name = " ";
        for (var e : button.getParent ( ).getChildrenUnmodifiable ( )) {
            if (e instanceof Label lbl && isNumber ( lbl.getText ( ) )) {
                price = lbl.getText ( );
            } else if (e instanceof Label lbl && !isNumber ( lbl.getText ( ) )) {
                name = lbl.getText ( );
            }
        }
        if (rol.equalsIgnoreCase ( "ADMINISTRADOR" )){
            HomeController homeController = context.getBean ( "HomeController", HomeController.class );
            homeController.getCartListView ( ).getItems ( ).addAll ( name + " $" + price );
        }else if (rol.equalsIgnoreCase ( "CLIENTE" )){
            HomeClientController homeClientController = context.getBean ( "HomeClientController", HomeClientController.class );
            homeClientController.getCartListView ( ).getItems ( ).addAll ( name + " $" + price );
        }else{
            HomeSellerController homeSellerController = context.getBean ( "HomeSellerController", HomeSellerController.class );
            homeSellerController.getCartListView ( ).getItems ( ).addAll ( name + " $" + price );
        }

    }

    public ProductDTO getProductByCart ( Button button ) {
        String price = "";
        String name = " ";
        ImageView imageView = null;
        for (var e : button.getParent ( ).getChildrenUnmodifiable ( )) {
            if (e instanceof Label lbl && isNumber ( lbl.getText ( ) )) {
                price = lbl.getText ( );
            } else if (e instanceof Label lbl && !isNumber ( lbl.getText ( ) )) {
                name = lbl.getText ( );
            } else if (e instanceof ImageView imageView1) {
                imageView = imageView1;
            }
        }
        if (imageView != null) {
            var img = imageView.getImage ( ).getUrl ();
            return productService.findProductByNameAndImgAndPrice ( name.replace ( " ", "_" ), extractProductName ( img ) , price );
        }
        return null;
    }
    public static String extractProductName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf("/") + 1;
        int nextSlashIndex = filePath.indexOf("/", lastSlashIndex);
        if (nextSlashIndex == -1) {
            nextSlashIndex = filePath.length ( );
        }

        return filePath.substring(lastSlashIndex, nextSlashIndex);
    }

    public void fillProducts ( VBox container, List<ProductDTO> productDTOS ) {
        String role = getClientByRol ().getRole ();
        switch (role.toUpperCase ()){
            case "ADMINISTRADOR"->{
                HomeController homeController = context.getBean ( "HomeController", HomeController.class );
                productDTOS = (List<ProductDTO>) getProductsByPage ( homeController.getContador ( ), (List<T>) productDTOS );
            }
            case "CLIENTE"->{
                HomeClientController homeController = context.getBean ( "HomeClientController", HomeClientController.class );
                productDTOS = (List<ProductDTO>) getProductsByPage ( homeController.getContador ( ), (List<T>) productDTOS );
            }
            case "VENDEDOR"->{
                HomeSellerController homeController = context.getBean ( "HomeSellerController", HomeSellerController.class );
                productDTOS = (List<ProductDTO>) getProductsByPage ( homeController.getContador ( ), (List<T>) productDTOS );
            }

        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool ( 1 );
        productDTOS = productDTOS.stream ( ).filter ( ProductDTO::isStatus ).toList ( );

        AtomicInteger count = new AtomicInteger ( 0 );
        AtomicBoolean eraser = new AtomicBoolean ( false );
        List<ProductDTO> finalProductDTOS = productDTOS;
        scheduler.schedule ( () -> {
            Platform.runLater ( () -> {
                container.getChildren ( ).stream ( )
                        .filter ( e -> e instanceof HBox )
                        .forEach ( e -> {
                            ((HBox) e).getChildren ( ).stream ( )
                                    .filter ( k -> k instanceof VBox )
                                    .forEach ( v -> {
                                        ProductDTO product = null;
                                        try {
                                            product = finalProductDTOS.get ( count.get ( ) );
                                        } catch (ArrayIndexOutOfBoundsException ex) {
                                            eraser.set ( true );
                                            clearProducts ( (VBox) v );
                                        }

                                        ProductDTO finalProduct = product;
                                        ((VBox) v).getChildren ( ).forEach ( controls -> {
                                            Platform.runLater ( () -> {
                                                if (eraser.get ( )) {
                                                    if (controls instanceof Label lbl && isNumber ( lbl.getText ( ) )) {
                                                        lbl.setText ( "0" );
                                                    } else if (controls instanceof Label lbl && !isNumber ( lbl.getText ( ) )) {
                                                        lbl.setText ( "" );
                                                    } else if (controls instanceof ImageView imageView) {
                                                        imageView.setImage ( null );
                                                    }

                                                } else if (finalProduct != null) {
                                                    if (controls instanceof Label lbl && lbl.getText ( ) != null && isNumber ( lbl.getText ( ) )) {
                                                        lbl.setText ( String.valueOf ( finalProduct.getPrice ( ) ) );
                                                    } else if (controls instanceof Label lbl && lbl.getText ( ) != null && !isNumber ( lbl.getText ( ) )) {
                                                        lbl.setText ( finalProduct.getName ( ) );
                                                    } else if (controls instanceof ImageView imageView) {
                                                        if (finalProduct.getImg ( ) != null && !finalProduct.getImg ( ).isEmpty ( )) {
                                                            try {
                                                                imageView.setImage ( new Image ( finalProduct.getImg ( ) ) );
                                                            } catch (IllegalArgumentException ex) {
                                                                System.out.println ( "Error al cargar la imagen: " + ex.getMessage ( ) );
                                                                imageView.setImage ( null );
                                                            }
                                                        } else {
                                                            imageView.setImage ( null );
                                                        }
                                                    }
                                                }
                                            } );
                                        } );
                                        eraser.set ( false );
                                        count.getAndIncrement ( );
                                    } );
                        } );
            } );
        }, 30, TimeUnit.MILLISECONDS );
    }


    void clearProducts ( VBox container ) {
        Platform.runLater ( () -> {
            container.getChildren ( ).forEach ( child -> {
                if (child instanceof Label lbl) {
                    if (lbl.getId ( ) != null && isNumber ( lbl.getText ( ) )) {
                        lbl.setText ( "0" );
                    } else {
                        lbl.setText ( "" );
                    }
                } else if (child instanceof ImageView imageView) {
                    imageView.setImage ( null );
                }

            } );
        } );
    }

    public List<T> getItemsByPage ( int numeroPagina, List<T> clients ) {
        int inicio = (numeroPagina - 1) * ITEMS_PER_PAGE;
        int fin = Math.min ( inicio + ITEMS_PER_PAGE, clients.size ( ) );
        this.items = clients;
        if (inicio >= clients.size ( )) {
            return new ArrayList<> ( );
        }

        return clients.subList ( inicio, fin );
    }

    public List<T> getProductsByPage ( int numeroPagina, List<T> products ) {
        int inicio = (numeroPagina - 1) * PRODUCT_PER_PAGE;
        int fin = Math.min ( inicio + PRODUCT_PER_PAGE, products.size ( ) );
        this.items = products;
        if (inicio >= products.size ( )) {
            return new ArrayList<> ( );
        }

        return products.subList ( inicio, fin );
    }

    public void loadImage ( TextField txtImage ) {
        FileChooser fileChooser = new FileChooser ( );
        fileChooser.getExtensionFilters ( ).add ( new FileChooser.ExtensionFilter ( "Text Files", "*.jpg",
                "*.png", "*.jpeg" ) );
        Stage stage = (Stage) txtImage.getScene ( ).getWindow ( );
        File file = fileChooser.showOpenDialog ( stage );
        if (file != null) {
            txtImage.setText ( "/static/media/images/products/" + file.getName ( ) );
        }
    }
    public ClientDTO getClientByRol(){
        var homeController = context.getBean ( "HomeController", HomeController.class );
        var currentUser = homeController.getCurrentUser ( );
        if( currentUser == null){
            var clientController = context.getBean ( "HomeClientController", HomeClientController.class );
            currentUser = clientController.getCurrentUser ();
            if (currentUser == null){
                var sellerController = context.getBean ( "HomeSellerController", HomeSellerController.class );
                currentUser = sellerController.getCurrentUser ();
            }
        }
        return currentUser;
    }
    public int totalPages ( List<T> clients ) {
        return (int) Math.ceil ( (double) clients.size ( ) / ITEMS_PER_PAGE );
    }

    public int totalProductsPage ( List<T> products ) {
        return (int) Math.ceil ( (double) products.size ( ) / PRODUCT_PER_PAGE );
    }

    @Autowired
    public void setLogger ( Logger logger ) {
        this.logger = logger;
    }

    public boolean permsValidate(String rol ){
        if(rol.equalsIgnoreCase ( "VENDEDOR" ) || rol.equalsIgnoreCase ( "CLIENTE" )){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alerta");
            alert.setHeaderText("Solo los administradores pueden editar el pedido.");
            alert.setContentText("Si necesitas realizar cambios en este pedido, por favor contacta con un administrador. Asegúrate de guardar todos los cambios pendientes antes de continuar.");
            alert.show();
            return true;
        }
        return false;
    }

    @Autowired
    public void setProductService ( ProductService productService ) {
        this.productService = productService;
    }
}
