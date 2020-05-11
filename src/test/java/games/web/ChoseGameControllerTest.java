package games.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = ChoseGameController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChoseGameControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGamesPageRedirection() throws Exception{
        mockMvc.perform(get("/games/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testGamesPage() throws Exception{
        mockMvc.perform(get("https://localhost:8443/games/")).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("List of Available Games")));
    }
}
