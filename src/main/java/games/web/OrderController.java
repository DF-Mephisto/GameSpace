package games.web;

import games.data.OrderRepository;
import games.data.UserRepository;
import games.entity.Order;
import games.entity.User;
import games.services.OrderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
@ConfigurationProperties(prefix = "order.orders")
public class OrderController {

    private OrderRepository OrderRepo;
    private UserRepository UserRepo;
    private OrderFactory OrderFact;

    private int pageSize = 1;
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

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
    public String orderList(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "orderid", defaultValue = "-1") Long orderid,
                            Model model)
    {
        Pageable pageable = PageRequest.of(page, pageSize);
        long pageCount = 1;

        List<Order> orders = new ArrayList<>();
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
        {
            switch(principal.getRole())
            {
                case "ROLE_USER":
                {
                    long userId = principal.getId();
                    OrderRepo.findAllByUser_id(userId, pageable).forEach(n -> orders.add(n));
                    pageCount = (long)Math.ceil((double)OrderRepo.countByUser_id(userId) / (double)pageSize);
                    break;
                }

                case "ROLE_ADMIN":
                {
                    if (orderid == -1) OrderRepo.findAll(pageable).forEach(n -> orders.add(n));
                    else
                    {
                        Optional<Order> order = OrderRepo.findById(orderid);
                        if (order.isPresent()) orders.add(order.get());
                        break;
                    }

                    pageCount = (long)Math.ceil((double)OrderRepo.count() / (double)pageSize);
                    break;
                }
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("orderid", orderid);

        return "orderList";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id)
    {
        OrderRepo.deleteById(id);

        return "redirect:/orders/all";
    }
}
