package games.web;

import games.data.GameRepository;
import games.data.UserRepository;
import games.entity.Game;
import games.security.UserRepositoryUserDetailsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ChoseGameController.class)
public class ChoseGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    @MockBean
    private GameRepository gameRepo;

    @Test
    public void testGamesPageRedirection() throws Exception{
        mockMvc.perform(get("/games/")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://localhost/games/"));
    }

    @Test
    public void testGamesPage() throws Exception{
        Game game = Mockito.mock(Game.class);
        List<Game> games = Arrays.asList(game, game, game, game);
        Mockito.when(gameRepo.findByNameIgnoreCaseContaining(any(), any())).thenReturn(games);

        mockMvc.perform(get("/games/").secure(true)).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("List of Available Games")))
                .andExpect(model().attributeExists("gameslist"))
                .andExpect(model().attribute("gameslist", Matchers.hasSize(4)))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("pageCount"))
                .andExpect(model().attributeExists("name"));
    }
}
