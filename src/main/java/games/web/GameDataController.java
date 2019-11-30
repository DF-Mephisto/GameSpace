package games.web;

import games.Game;
import games.data.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/app")
public class GameDataController {

    private GameRepository GameRepo;

    @Autowired
    public GameDataController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @GetMapping("/*/*")
    public String showGameData(Model model, HttpServletRequest request)
    {
        Long id;

        try{
            String url = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().lastIndexOf('/'));
            id = Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
        }
        catch(NumberFormatException e)
        {
            return "redirect:/games";
        }

        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            model.addAttribute("game", res.get());
            return "game";
        }

        return "redirect:/games";
    }
}