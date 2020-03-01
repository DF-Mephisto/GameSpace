package games.web;

import games.data.OrderRepository;
import games.data.UserRepository;
import games.entity.Order;
import games.entity.User;
import games.services.OrderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderRepository OrderRepo;
    private UserRepository UserRepo;
    private OrderFactory OrderFact;

    @Autowired
    public OrderController(OrderRepository OrderRepo, UserRepository UserRepo,
                           OrderFactory OrderFact)

    {
        this.OrderRepo = OrderRepo;
        this.UserRepo = UserRepo;
        this.OrderFact = OrderFact;
    }

    @ModelAttribute(name = "order")
    public Order order(@CookieValue(value = "items", defaultValue = "") String items)
    {
        return OrderFact.makeOrder(items);
    }

    @GetMapping("/current")
    public String orderForm()
    {
        return "orderForm";
    }

    @PostMapping("/current")
    public String processOrder(@Valid @ModelAttribute("order") Order order, BindingResult errors,
                               SessionStatus sessionStatus, HttpServletResponse response)
    {

        if (errors.hasErrors())
        {
            return "orderForm";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = UserRepo.findByUsername(((UserDetails) principal).getUsername());
            if (user != null)
            {
                order.setUser(user);
                user.getOrders().add(order);

                OrderRepo.save(order);
            }
        }


        Cookie cookie = new Cookie("items", null);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        sessionStatus.setComplete();

        return "redirect:/games";
    }

    @GetMapping("/all")
    public String orderList(Model model)
    {
        List<Order> orders = new ArrayList<>();
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
        {
            switch(principal.getRole())
            {
                case "ROLE_USER":
                {
                    OrderRepo.findAllByUser_id(principal.getId()).forEach(n -> orders.add(n));
                    break;
                }

                case "ROLE_ADMIN":
                {
                    OrderRepo.findAll().forEach(n -> orders.add(n));
                    break;
                }
            }
        }

        model.addAttribute("orders", orders);

        return "orderList";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id)
    {
        OrderRepo.deleteById(id);

        return "redirect:/orders/all";
    }
}
