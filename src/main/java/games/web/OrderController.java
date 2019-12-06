package games.web;

import games.data.OrderRepository;
import games.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OrderController(OrderRepository OrderRepo)
    {
        this.OrderRepo = OrderRepo;
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

    @GetMapping("/all")
    public String orderList(Model model)
    {
        List<Order> orders = new ArrayList<>();
        OrderRepo.findAll().forEach(n -> orders.add(n));
        model.addAttribute("orders", orders);

        return "orderList";
    }

    @PostMapping
    public String processOrder(@Valid Order order, BindingResult errors, SessionStatus sessionStatus)
    {

        if (errors.hasErrors())
        {
            return "orderForm";
        }

        OrderRepo.save(order);
        sessionStatus.setComplete();

        return "redirect:/games";
    }
}
