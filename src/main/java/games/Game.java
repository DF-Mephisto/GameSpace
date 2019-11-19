package games;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String name;
    private String genre;
    private String date;
    private String dev;
    private int Price;
    private String desc;
    private byte[] image;
    private String ext;
}
