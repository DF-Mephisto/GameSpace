package games.security;

import games.data.UserRepository;
import games.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository UserRepo;
    private PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository UserRepo, PasswordEncoder encoder)
    {
        this.UserRepo = UserRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String registerForm(Model model)
    {
        model.addAttribute("regForm", new RegistrationForm());

        return "registerForm";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("regForm") RegistrationForm regForm, BindingResult errors)
    {
        try {
            User user = UserRepo.findByUsername(regForm.getUsername());

            if (user != null) throw new IOException("User already exists");

        } catch (IOException e)
        {
            errors.rejectValue("username", "error.regForm", "User with the same name already exists");
        }

        if (errors.hasErrors())
        {
            return "registerForm";
        }

        UserRepo.save(regForm.toUser(encoder));
        return "redirect:/login";
    }
}
