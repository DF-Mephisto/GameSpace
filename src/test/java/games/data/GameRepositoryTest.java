package games.data;

import games.entity.Game;
import games.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = GameRepository.class)
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepo;

    private List<Game> testGames;

    private Game gameBuilder(String name, String genre, String date, String dev, String price, String desc)
    {
        Game game = new Game();
        game.setName(name);
        game.setDate(date);
        game.setGenre(genre);
        game.setDev(dev);
        game.setPrice(price);
        game.setDesc(desc);

        return game;
    }

    @BeforeEach
    void setup()
    {
        testGames = new ArrayList<>();

        Game testGame = gameBuilder("testGame", "ignore", "1970-01-01", "ignore", "0", "ignore");
        testGames.add(testGame);

        testGame = gameBuilder("gameTest", "ignore", "1970-01-01", "ignore", "0", "ignore");
        testGames.add(testGame);

        testGame = gameBuilder("another Game", "ignore", "1970-01-01", "ignore", "0", "ignore");
        testGames.add(testGame);

        gameRepo.saveAll(testGames);
    }

    @Test
    void repoIsNotNull()
    {
        assertThat(gameRepo).isNotNull();
    }

    @Test
    void testFindAll()
    {
        Iterable<Game> games = gameRepo.findAll();
        assertThat(games).hasSize(3);
        assertThat(games).isEqualTo(testGames);
    }

    @Test
    void testFindByNameContainingString()
    {
        List<Game> games = gameRepo
                .findByNameIgnoreCaseContaining("test", PageRequest.of(0, 10));

        assertThat(games).hasSize(2);
        assertThat(games).contains(testGames.get(0)).contains(testGames.get(1));
    }

    @Test
    void testCountByNameContainingString()
    {
        assertThat(gameRepo.countByNameIgnoreCaseContaining("another")).isEqualTo(1);
    }
}
