package games;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Constraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {
    @NotNull
    @Size(min=1, max=50, message = "Name must be between 1 and 50 length long")
    String name;

    @NotNull
    @Size(min=1, max=50, message = "Genre must be between 1 and 50 length long")
    String genre;

    @NotNull
    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-[0-2][0-9]|3[0-1])", message="Incorrect date")
    String date;

    @NotNull
    @Size(min=1, max=50, message = "Developer must be between 1 and 50 length long")
    String dev;

    @NotNull
    @Pattern(regexp = "([0-9]+)", message = "Price must be integer value")
    String price;

    @NotNull
    @Size(min=1, max=1000, message = "Description must be between 1 and 50 length long")
    String desc;

    byte[] image;
    String ext;
}
