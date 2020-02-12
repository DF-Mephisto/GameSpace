package games.web;

import games.data.GameRepository;
import games.entity.Game;
import games.entity.Order;
import games.services.OrderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@SessionAttributes("order")
public class CartController {

    private GameRepository GameRepo;
    private OrderFactory OrderFact;

    @Autowired
    public CartController(GameRepository GameRepo, OrderFactory OrderFact)
    {
        this.GameRepo = GameRepo;
        this.OrderFact = OrderFact;
    }

    @ModelAttribute(name = "order")
    public Order order(@CookieValue(value = "items", defaultValue = "") String items)
    {
        return OrderFact.makeOrder(items);
    }

    @GetMapping
    public String orderForm()
    {
        return "cart";
    }

    @PostMapping
    public String AddToCart(@RequestParam("id") Long id, @ModelAttribute Order order,
                            @CookieValue(value = "items", defaultValue = "") String items,
                            HttpServletResponse response)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            order.setTotal_price(order.getTotal_price() + Integer.valueOf(res.get().getPrice()));
            order.addGame(res.get());

            Cookie cookie = new Cookie("items", items + res.get().getId() + '/');
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/cart";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Integer id, @ModelAttribute Order order,
                              @CookieValue(value = "items", defaultValue = "") String items,
                              HttpServletResponse response)
    {
        order.setTotal_price(order.getTotal_price() - Integer.valueOf(order.getGames().get(id).getPrice()));
        order.getGames().remove((int)id);

        List<String> itemList = new ArrayList(Arrays.asList(items.split("/")));
        itemList.remove((int)id);

        Cookie cookie = new Cookie("items", itemList.stream().reduce("", (str1, str2) -> str1 + str2 + '/'));
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/cart";
    }
}
