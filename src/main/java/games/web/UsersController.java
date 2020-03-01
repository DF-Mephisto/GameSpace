package games.web;

import games.data.UserRepository;
import games.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UsersController {

    private UserRepository UserRepo;

    @Autowired
    public UsersController(UserRepository UserRepo)
    {
        this.UserRepo = UserRepo;
    }

    @GetMapping
    public String getUsers(Model model)
    {
        List<User> users = UserRepo.findAllExceptRole("ROLE_ADMIN", Sort.by(Sort.Direction.ASC, "username"));

        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("{id}/lock")
    public String lockUser(@PathVariable("id") Long id)
    {
        Optional<User> user = UserRepo.findById(id);
        if (user.isPresent())
        {
            User u = user.get();
            u.setNonLocked(!u.getNonLocked());
            UserRepo.save(u);
        }

        return "redirect:/users";
    }
}
