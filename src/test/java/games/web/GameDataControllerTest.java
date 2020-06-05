package games.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import games.data.CommentRepository;
import games.data.GameRepository;
import games.data.UserRepository;
import games.entity.Comment;
import games.entity.Game;
import games.entity.Order;
import games.entity.User;
import games.security.UserRepositoryUserDetailsService;
import games.services.OrderFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameDataController.class)
public class GameDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    @MockBean
    private GameRepository gameRepo;

    @MockBean
    private CommentRepository commRepo;

    @MockBean
    private OrderFactory orderFact;

    @Test
    public void testShowGameDataIfExists() throws Exception{
        Game game = new Game();
        game.setName("test game");

        Mockito.when(gameRepo.findById(anyLong())).thenReturn(Optional.ofNullable(game));
        Mockito.when(orderFact.makeOrder(any())).thenReturn(Mockito.mock(Order.class));

        mockMvc.perform(get("/app/{id}/test game", 1).secure(true))
                .andExpect(status().isOk())
                .andExpect(view().name("game"))
                .andExpect(content().string(Matchers.containsString("Description")))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attribute("game", Matchers.equalTo(game)))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void testShowGameDataIfNotExists() throws Exception{
        Mockito.when(gameRepo.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(get("/app/{id}/test game", 1).secure(true))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));
    }

    @Test
    public void testDeleteGame() throws Exception{
        String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        mockMvc.perform(post("/app/delete").param("id", "1").secure(true)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));
    }

    @Test
    @WithMockUser("testuser")
    public void testAddCommentWhenGameExists() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        Game game = new Game();
        game.setId(Long.valueOf(1));
        game.setName("testGame");

        Comment comment = new Comment();
        comment.setText("test comment");

        Mockito.when(gameRepo.findById(anyLong())).thenReturn(Optional.ofNullable(game));
        Mockito.when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(Mockito.mock(User.class)));

        RequestBuilder request = post("/app/comment")
                .param("comment", mapper.writeValueAsString(comment))
                .param("id", "1")
                .secure(true)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/app/" + game.getId() + "/" + game.getName()));
    }

    @Test
    @WithMockUser("testuser")
    public void testAddCommentWhenGameNotExists() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        Comment comment = new Comment();

        Mockito.when(gameRepo.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        RequestBuilder request = post("/app/comment")
                .param("comment", mapper.writeValueAsString(comment))
                .param("id", "1")
                .secure(true)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));
    }

    @Test
    public void testDeleteComment() throws Exception{
        Long gameId = Long.valueOf(1);
        String name = "testGame";

        RequestBuilder request = post("/app/{gameId}/{gameName}/comment/{commentId}/delete", gameId, name, 1)
                .secure(true)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/" + gameId + "/" + name));
    }
}
