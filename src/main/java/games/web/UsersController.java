package games.web;

import games.data.UserRepository;
import games.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@ConfigurationProperties(prefix = "user.users")
public class UsersController {

    private int pageSize = 1;
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    private UserRepository UserRepo;

    @Autowired
    public UsersController(UserRepository UserRepo)
    {
        this.UserRepo = UserRepo;
    }

    @GetMapping
    public String getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                           Model model)
    {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "username"));
        long pageCount = 1;

        List<User> users = UserRepo.findAllExceptRole("ROLE_ADMIN", pageable);
        pageCount = (long)Math.ceil((double)UserRepo.countAllExceptRole("ROLE_ADMIN") / (double)pageSize);

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);

        return "users";
    }

    @PostMapping("{id}/lock")
    public String lockUser(@PathVariable("id") Long id, @RequestParam("page") Long page)
    {
        Optional<User> user = UserRepo.findById(id);
        if (user.isPresent())
        {
            User u = user.get();
            u.setNonLocked(!u.getNonLocked());
            UserRepo.save(u);
        }

        return "redirect:/users?page=" + page;
    }
}
