package games.web;

import games.entity.Game;
import games.data.GameRepository;
import games.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/app")
@SessionAttributes("order")
public class GameDataController {

    private GameRepository GameRepo;

    @Autowired
    public GameDataController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @ModelAttribute(name = "order")
    public Order order()
    {
        return new Order();
    }

    @GetMapping("/{id}/*")
    public String showGameData(Model model, @PathVariable("id") Long id)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            model.addAttribute("game", res.get());
            return "game";
        }

        return "redirect:/games";
    }

    @PostMapping("/*/*")
    public String AddToCart(@RequestParam("id") Long id, @ModelAttribute Order order)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            order.addGame(res.get());
        }

        return "redirect:/orders/current";
    }
}