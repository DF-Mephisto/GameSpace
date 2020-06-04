package games.web;

import games.data.GameRepository;
import games.data.UserRepository;
import games.entity.Game;
import games.security.SecurityConfig;
import games.security.ServerConfig;
import games.security.UserRepositoryUserDetailsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
    UserRepository userRepo;

    @MockBean
    UserRepositoryUserDetailsService userRepoService;

    @MockBean
    GameRepository gameRepo;

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
