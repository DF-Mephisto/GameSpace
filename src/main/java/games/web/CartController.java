package games.web;

import games.data.GameRepository;
import games.entity.Game;
import games.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@SessionAttributes("order")
public class CartController {

    private GameRepository GameRepo;

    @Autowired
    public CartController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @ModelAttribute(name = "order")
    public Order order()
    {
        return new Order();
    }

    @GetMapping
    public String orderForm()
    {
        return "cart";
    }

    @PostMapping
    public String AddToCart(@RequestParam("id") Long id, @ModelAttribute Order order)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            order.setTotal_price(order.getTotal_price() + Integer.valueOf(res.get().getPrice()));
            order.addGame(res.get());
        }

        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Integer id, @ModelAttribute Order order)
    {
        order.setTotal_price(order.getTotal_price() - Integer.valueOf(order.getGames().get(id).getPrice()));
        order.getGames().remove((int)id);

        return "redirect:/cart";
    }
}
