package games;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Street is required")
    String street;

    @NotBlank(message = "City is required")
    String city;

    @NotBlank(message = "House is required")
    String house;

    @CreditCardNumber(message = "Not a valid credit card number")
    String ccNumber;

    @Pattern(regexp = "^((0[1-9]|1[0-2])([\\/])[1-9][0-9])", message="Must be formatted MM/YY")
    String ccExpiration;

    @Digits(integer=3, fraction = 0, message = "Invalid CVV")
    String ccCVV;
}
