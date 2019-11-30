package games.web;

import games.Game;
import games.data.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/games")
public class ChoseGameController {

    private GameRepository GameRepo;

    @Autowired
    public ChoseGameController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @GetMapping
    public String showGameList(Model model)
    {
        List<Game> games = new ArrayList<>();
        GameRepo.findAll().forEach(n -> games.add(n));
        model.addAttribute("gameslist", games);
        return "games";
    }
}
