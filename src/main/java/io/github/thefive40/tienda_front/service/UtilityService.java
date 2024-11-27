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
/**
 * UtilityService is a generic service class providing various utility methods for handling
 * clients, products, and user interactions in the application.
 * This class supports operations such as pagination, product display, file handling,
 * and permission validation.
 *
 * @param <T> The generic type representing items managed by this service.
 */
@Service
@Getter
@Scope("prototype")
public class UtilityService<T> {

    /**
     * List of items managed by the service.
     */
    private List<T> items;
    /**
     * Number of items per page for pagination.
     */
    @Setter
    private static int ITEMS_PER_PAGE = 5;

    /**
     * Number of products per page for pagination.
     */
    private static int PRODUCT_PER_PAGE = 8;

    /**
     * Logger instance for logging events and errors.
     */
    private Logger logger;

    /**
     * Service for handling product-related operations.
     */
    private ProductService productService;

    /**
     * Spring's application context for accessing beans.
     */
    @Autowired
    private ApplicationContext context;

    /**
     * Checks if the given string is a numeric value.
     *
     * @param text the string to check.
     * @return {@code true} if the string is numeric; {@code false} otherwise.
     */
    public boolean isNumber ( String text ) {
        try {
            Double.parseDouble ( text );
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Retrieves an item by its position on a specific page.
     *
     * @param button the button triggering the request.
     * @param page   the page number.
     * @return the item of type {@code T}.
     */
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
    /**
     * Adds a product to the user's cart based on their role.
     *
     * @param button the button triggering the addition.
     */
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
    /**
     * Retrieves the product details from the cart based on the button's parent container.
     *
     * @param button the button triggering the request.
     * @return a {@link ProductDTO} object representing the product.
     */
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
    /**
     * Extracts the product name from the file path.
     *
     * @param filePath the file path to extract the name from.
     * @return the extracted product name.
     */
    public static String extractProductName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf("/") + 1;
        int nextSlashIndex = filePath.indexOf("/", lastSlashIndex);
        if (nextSlashIndex == -1) {
            nextSlashIndex = filePath.length ( );
        }

        return filePath.substring(lastSlashIndex, nextSlashIndex);
    }
    /**
     * Fills a container with products and updates the UI dynamically.
     *
     * @param container  the container to fill.
     * @param productDTOS the list of {@link ProductDTO} objects.
     */
    public void fillProducts ( VBox container, List<ProductDTO> productDTOS ) {
        String role = "";
        try{
           role  = getClientByRol ().getRole ();
        }catch(NullPointerException exception){}
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
    /**
     * Populates a container with product data or clears it if the clear flag is set.
     *
     * @param container the {@link VBox} container to populate.
     * @param product   the {@link ProductDTO} object containing product data.
     * @param clearFlag an {@link AtomicBoolean} flag indicating whether to clear the container.
     */
    public void populateProduct(VBox container, ProductDTO product, AtomicBoolean clearFlag) {
        container.getChildren().forEach(node -> {
            if (clearFlag.get()) {
                clearProductNode(node);
            } else if (product != null) {
                updateProductNode(node, product);
            }
        });
    }

    /**
     * Clears the content of a node in the container.
     *
     * @param node the node to clear (either a {@link Label} or {@link ImageView}).
     */
    public void clearProductNode(Object node) {
        if (node instanceof Label lbl) {
            lbl.setText(lbl.getId() != null && isNumber(lbl.getText()) ? "0" : "");
        } else if (node instanceof ImageView imageView) {
            imageView.setImage(null);
        }
    }
    /**
     * Updates a node in the container with product data.
     *
     * @param node    the node to update (either a {@link Label} or {@link ImageView}).
     * @param product the {@link ProductDTO} object containing product data.
     */
    public void updateProductNode(Object node, ProductDTO product) {
        if (node instanceof Label lbl) {
            if (isNumber(lbl.getText())) lbl.setText(String.valueOf(product.getPrice()));
            else lbl.setText(product.getName());
        } else if (node instanceof ImageView imgView) {
            imgView.setImage(new Image(product.getImg()));
        }
    }

    /**
     * Clears all products from the container.
     *
     * @param container the container to clear.
     */
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
    /**
     * Retrieves a sublist of items based on the current page number.
     *
     * @param numeroPagina the page number.
     * @param clients      the list of items to paginate.
     * @return a {@link List} containing the items for the specified page.
     */
    public List<T> getItemsByPage ( int numeroPagina, List<T> clients ) {
        int inicio = (numeroPagina - 1) * ITEMS_PER_PAGE;
        int fin = Math.min ( inicio + ITEMS_PER_PAGE, clients.size ( ) );
        this.items = clients;
        if (inicio >= clients.size ( )) {
            return new ArrayList<> ( );
        }

        return clients.subList ( inicio, fin );
    }

    /**
     * Retrieves a sublist of products based on the current page number.
     *
     * @param numeroPagina the page number.
     * @param products     the list of products to paginate.
     * @return a {@link List} containing the products for the specified page.
     */
    public List<T> getProductsByPage ( int numeroPagina, List<T> products ) {
        int inicio = (numeroPagina - 1) * PRODUCT_PER_PAGE;
        int fin = Math.min ( inicio + PRODUCT_PER_PAGE, products.size ( ) );
        this.items = products;
        if (inicio >= products.size ( )) {
            return new ArrayList<> ( );
        }

        return products.subList ( inicio, fin );
    }
    /**
     * Opens a file chooser to select an image and sets the file path to the given text field.
     *
     * @param txtImage the {@link TextField} to set the file path.
     */
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
    /**
     * Retrieves the current user based on their role.
     *
     * @return a {@link ClientDTO} object representing the current user.
     */
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

    /**
     * Calculates the total number of pages for a list of items.
     *
     * @param item the list of items.
     * @return the total number of pages.
     */
    public int totalPages ( List<T> item ) {
        return (int) Math.ceil ( (double) item.size ( ) / ITEMS_PER_PAGE );
    }
    /**
     * Calculates the total number of pages for a list of products.
     *
     * @param products the list of products.
     * @return the total number of pages.
     */
    public int totalProductsPage ( List<T> products ) {
        return (int) Math.ceil ( (double) products.size ( ) / PRODUCT_PER_PAGE );
    }
    /**
     * Sets the logger instance.
     *
     * @param logger the {@link Logger} to set.
     */
    @Autowired
    public void setLogger ( Logger logger ) {
        this.logger = logger;
    }
    /**
     * Validates permissions for the given role.
     * Displays a warning alert if the role is not allowed to perform an action.
     *
     * @param rol the role to validate.
     * @return {@code true} if the role is restricted; {@code false} otherwise.
     */
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
    /**
     * Sets the {@link ProductService} instance.
     *
     * @param productService the {@link ProductService} to set.
     */
    @Autowired
    public void setProductService ( ProductService productService ) {
        this.productService = productService;
    }
}
