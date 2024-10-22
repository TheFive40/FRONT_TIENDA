package io.github.thefive40.tienda_front.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Data Transfer Object (DTO) class for handling user information.
 * This class includes validations for fields such as email and password
 * and supports image handling as a byte array.
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class UserDTO implements Serializable, Cloneable {
    /**
     * User's email address.
     * Validated using the @Email annotation.
     */
    @Email
    private String email;
    /**
     * User's password.
     * It must meet the following regular expression criteria:
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one number
     * - At least one special character
     * The password must be a minimum of 8 characters.
     */
    @Pattern(regexp = "^(?:(?=.*[A-Z])(?=.*[a-z]).{12,}$|(?=.*[A-Z])(?=.*[a-z])(?=(.*\\d){3,}).{5,}$|(?=.*[a-z])(?=.*[@$!%*?&]).{2,}$|(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{2,}$|(?=.*\\d)(?=.*[@$!%*?&]).{2,}$|(?=.*[A-Z])(?=.*[a-z])(?=(.*\\d){3,})(?=.*[@$!%*?&]).{9,}$)$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private byte[] image;
    /**
     * Constructor for creating a UserDTO with email and password only.
     *
     * @param email    User's email address.
     * @param password User's password.
     */
    public UserDTO ( String email, String password ) {
        this.email = email;
        this.password = password;
    }
    /**
     * Constructor for creating a UserDTO with additional personal information.
     *
     * @param email    User's email address.
     * @param password User's password.
     * @param name     User's first name.
     * @param lastName User's last name.
     * @param phone    User's phone number.
     */
    public UserDTO ( String email, String password, String name, String lastName, String phone ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Loads an image from the specified file path and stores it as a byte array.
     *
     * @param url The file path of the image.
     */
    public void saveImage ( String url ) {
        Path path = Paths.get ( url );
        try {
            byte[] fileByte = Files.readAllBytes ( path);
            setImage ( fileByte );
        } catch (IOException e) {
            throw new RuntimeException ( e );

        }
    }
    /**
     * Creates a copy of this UserDTO object.
     *
     * @return A cloned copy of the current UserDTO.
     */
    @Override
    public UserDTO clone () {
        try {
            // TODO: copy mutable
            //  state here, so the clone can't change the internals of the original
            return (UserDTO) super.clone ( );
        } catch (CloneNotSupportedException e) {
            throw new AssertionError ( );
        }
    }
}
