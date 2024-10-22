package io.github.thefive40.tienda_front.service;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
@Service
public class ReadImageService {

    public ImageView readImage ( byte[] hash ) {
        return new ImageView ( convertToImage ( hash ) );
    }

    public Image convertToImage ( byte[] hash ) {
        return new Image ( new ByteArrayInputStream ( hash ) );
    }

}
