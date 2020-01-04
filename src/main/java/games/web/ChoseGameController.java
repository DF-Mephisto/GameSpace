package games.web;

import games.entity.Game;
import games.data.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/games")
@ConfigurationProperties(prefix = "games.games")
public class ChoseGameController {

    private GameRepository GameRepo;
    private int pageSize = 1;

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    @Autowired
    public ChoseGameController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @GetMapping
    public String showGameList(@RequestParam(value = "page", defaultValue = "0") int page, Model model)
    {
        Pageable pageable = PageRequest.of(page, pageSize);

        List<Game> games = new ArrayList<>();
        GameRepo.findAll(pageable).forEach(n -> games.add(n));

        long pageCount = (long)Math.ceil((double)GameRepo.count() / (double)pageSize);

        model.addAttribute("gameslist", games);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        return "games";
    }
}
