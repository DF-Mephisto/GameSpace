package games.web;

import games.entity.Game;
import games.data.GameRepository;
import games.entity.Order;
import games.services.OrderFactory;
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
    private OrderFactory OrderFact;

    @Autowired
    public GameDataController(GameRepository GameRepo, OrderFactory OrderFact)
    {
        this.GameRepo = GameRepo;
        this.OrderFact = OrderFact;
    }

    @ModelAttribute(name = "order")
    public Order order(@CookieValue(value = "items", defaultValue = "") String items)
    {
        return OrderFact.makeOrder(items);
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

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id)
    {
       GameRepo.deleteById(id);

       return "redirect:/games";
    }
}