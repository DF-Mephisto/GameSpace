package games.web;

import games.Game;
import games.data.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/add")
public class AddGameController {

    private GameRepository GameRepo;

    @Autowired
    public AddGameController(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    @ModelAttribute(name = "game")
    public Game game()
    {
        return new Game();
    }

    @GetMapping
    public String addGame()
    {
        return "add";
    }

    @PostMapping
    public String processNewGame(@Valid Game game, BindingResult errors, @RequestParam("file") MultipartFile file)
    {
        try {
            if (file.isEmpty()) throw new NullPointerException("No image");

            game.setImage(Base64.getEncoder().encode(file.getBytes()));
            game.setExt(Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1)).toString());

        } catch (IOException e) {
            errors.rejectValue("image", "error.game", "File uploading error");
        } catch (NullPointerException e)
        {
            errors.rejectValue("image", "error.game", "You must choose an image");
        }


        if (errors.hasErrors())
        {
            return "add";
        }

        GameRepo.save(game);
        log.info("Processing new game: " + game);

        return "redirect:/orders/current";
    }
}
