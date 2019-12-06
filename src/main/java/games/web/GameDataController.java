package games.web;

import games.entity.Game;
import games.data.GameRepository;
import games.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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