package games.web;

import games.data.OrderRepository;
import games.data.UserRepository;
import games.entity.Order;
import games.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

    @Autowired
    public OrderController(OrderRepository OrderRepo, UserRepository UserRepo)

    {
        this.OrderRepo = OrderRepo;
        this.UserRepo = UserRepo;
    }

    @ModelAttribute(name = "order")
    public Order order()
    {
        return new Order();
    }

    @GetMapping("/current")
    public String orderForm()
    {
        return "orderForm";
    }

    @PostMapping("/current")
    public String processOrder(@Valid @ModelAttribute("order") Order order, BindingResult errors, SessionStatus sessionStatus)
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

        sessionStatus.setComplete();

        return "redirect:/games";
    }

    @GetMapping("/all")
    public String orderList(Model model)
    {
        List<Order> orders = new ArrayList<>();
        OrderRepo.findAll().forEach(n -> orders.add(n));
        model.addAttribute("orders", orders);

        return "orderList";
    }
}
