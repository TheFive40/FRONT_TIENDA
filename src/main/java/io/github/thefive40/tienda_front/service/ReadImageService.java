package io.github.thefive40.tienda_front.service;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
/**
 * ReadImageService is a service class responsible for converting image data represented
 * as a byte array into JavaFX `Image` and `ImageView` objects. This class provides
 * utility methods for handling image data for display in JavaFX applications.
 */
@Service
public class ReadImageService {
    /**
     * Converts a byte array representing an image into an {@link ImageView}.
     *
     * @param hash the byte array containing the image data.
     * @return an {@link ImageView} object created from the byte array.
     */
    public ImageView readImage ( byte[] hash ) {
        return new ImageView ( convertToImage ( hash ) );
    }
    /**
     * Converts a byte array representing an image into a JavaFX {@link Image}.
     *
     * @param hash the byte array containing the image data.
     * @return an {@link Image} object created from the byte array.
     */
    public Image convertToImage ( byte[] hash ) {
        return new Image ( new ByteArrayInputStream ( hash ) );
    }

}
