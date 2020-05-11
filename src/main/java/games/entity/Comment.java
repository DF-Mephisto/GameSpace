package games.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank
    @Size(min=1, max=1000, message = "Comment must be between 1 and 1000 length long")
    String text;

    @Column(name="placedat")
    Date placedAt;

    @ManyToOne
    @JoinColumn(name = "game_id")
    Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @PrePersist
    public void placedAt()
    {
        this.placedAt = new Date();
    }
}
