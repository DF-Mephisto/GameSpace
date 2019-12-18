package games.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="game_order", schema = "public")
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank
    @Size(min=1, max=50, message = "Name must be between 1 and 50 length long")
    String name;

    @NotBlank
    @Size(min=1, max=50, message = "Street must be between 1 and 50 length long")
    String street;

    @NotBlank
    @Size(min=1, max=50, message = "City must be between 1 and 50 length long")
    String city;

    @NotBlank
    @Size(min=1, max=10, message = "House must be between 1 and 10 length long")
    String house;

    @Column(name="ccnumber")
    @CreditCardNumber(message = "Not a valid credit card number")
    String ccNumber;

    @Column(name="ccexpiration")
    @Pattern(regexp = "^((0[1-9]|1[0-2])([\\/])[1-9][0-9])", message="Must be formatted MM/YY")
    String ccExpiration;

    @Column(name="cccvv")
    @Digits(integer=3, fraction = 0, message = "Invalid CVV")
    String ccCVV;

    @Column(name="placedat")
    Date placedAt;

    @ManyToMany(targetEntity = Game.class)
    @NotEmpty(message = "You must choose at least one game")
    List<Game> games = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public void addGame(Game game)
    {
        games.add(game);
    }

    @PrePersist
    public void placedAt()
    {
        this.placedAt = new Date();
    }
}
