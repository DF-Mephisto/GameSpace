package games.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="game", schema = "public")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank
    @Size(min=1, max=50, message = "Name must be between 1 and 50 length long")
    String name;

    @NotBlank
    @Size(min=1, max=50, message = "Genre must be between 1 and 50 length long")
    String genre;

    @Pattern(regexp = "([0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1]))", message="Incorrect date")
    String date;

    @NotBlank
    @Size(min=1, max=50, message = "Developer must be between 1 and 50 length long")
    String dev;

    @Pattern(regexp = "([0-9]+)", message = "Price must be integer value")
    String price;

    @Column(name="description")
    @NotBlank
    @Size(min=1, max=1000, message = "Description must be between 1 and 1000 length long")
    String desc;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Screen> screens = new ArrayList<>();

    byte[] image;
    String ext;
    public String getImageStr()
    {
        return "data:image/" + getExt() + ";base64," + new String(getImage());
    }
}
