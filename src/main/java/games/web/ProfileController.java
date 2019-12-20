package games.web;

import games.data.UserRepository;
import games.entity.User;
import games.forms.ChangeProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserRepository UserRepo;
    private PasswordEncoder encoder;

    @Autowired
    public ProfileController(UserRepository UserRepo, PasswordEncoder encoder)
    {
        this.UserRepo = UserRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String profile(Model model)
    {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ChangeProfileForm form = new ChangeProfileForm();

        if (principal instanceof UserDetails)
        {
            form.setUsername(principal.getUsername());
        }

        model.addAttribute("form", form);
        return "profile";
    }

    @PostMapping
    public String changeProfile(@Valid @ModelAttribute("form") ChangeProfileForm form, BindingResult errors)
    {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            User user = UserRepo.findByUsername(form.getUsername());
            if (user != null && !principal.getUsername().equals(user.getUsername()))
            {
                throw new IOException("User already exists");
            }

        } catch (IOException e)
        {
            errors.rejectValue("username", "error.form", "User with the same name already exists");
        }

        try {
            if (!encoder.matches(form.getOldPassword(), principal.getPassword()))
            {
                throw new IOException("Wrong password");
            }

        } catch (IOException e)
        {
            errors.rejectValue("oldPassword", "error.form", "Wrong password");
        }

        if (errors.hasErrors())
        {
            return "profile";
        }

        principal.setUsername(form.getUsername());
        principal.setPassword(encoder.encode(form.getPassword()));
        UserRepo.save(principal);

        return "redirect:/";
    }
}
