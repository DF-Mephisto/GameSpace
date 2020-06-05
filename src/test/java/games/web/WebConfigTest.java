package games.web;

import games.data.UserRepository;
import games.security.SecurityConfig;
import games.security.UserRepositoryUserDetailsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class})
public class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    @Test
    public void testHomePageRedir() throws Exception{
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://localhost/"));
    }

    @Test
    public void testHomePage() throws Exception{
        mockMvc.perform(get("/").secure(true))
                .andExpect(view().name("home"))
                .andExpect(content().string(Matchers.containsString("Welcome to")));
    }
}
