package games;

import games.data.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePageRedirection() throws Exception{
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testHomePage() throws Exception{
        mockMvc.perform(get("https://localhost:8443/")).andExpect(status().isOk()).andExpect(view().name("home"))
                .andExpect(content().string(Matchers.containsString("Welcome to")));
    }
}
