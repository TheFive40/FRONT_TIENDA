package io.github.thefive40.tienda_front.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class ClientDTO implements Serializable, Cloneable {
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
    private Long idClient;
    @NotNull
    private String name;
    @NotNull
    private String lastname;

    private String phone;

    private String url;

    private String secret_key;

    private String initVector;

    private Date registrationDate;

    private String role;

    private boolean status;

    private List<ProductDTO> products = new ArrayList<> ();

    private List<OrderDTO> orders = new ArrayList<> ();

    private List<ReviewDTO> reviews = new ArrayList<> ();

    private List<ShoppingCartDTO> shoppingCart = new ArrayList<> ();

    /**
     * Constructor for creating a UserDTO with email and password only.
     *
     * @param email    User's email address.
     * @param password User's password.
     */
    public ClientDTO ( String email, String password ) {
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
    public ClientDTO ( String email, String password, String name, String lastName, String phone ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastname = lastName;
        this.phone = phone;
        this.registrationDate = new Date (  );
    }


    /**
     * Creates a copy of this UserDTO object.
     *
     * @return A cloned copy of the current UserDTO.
     */
    @Override
    public ClientDTO clone () {
        try {
            // TODO: copy mutable
            //  state here, so the clone can't change the internals of the original
            return (ClientDTO) super.clone ( );
        } catch (CloneNotSupportedException e) {
            throw new AssertionError ( );
        }
    }
}
