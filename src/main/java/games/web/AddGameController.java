package games.web;

import games.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/add")
public class AddGameController {

    @GetMapping
    public String addGame(Model model)
    {

        model.addAttribute("game", new Game());

        return "add";
    }

    @PostMapping
    public String processNewGame(Game game, @RequestParam("file") MultipartFile file, Model model)
    {
        try {
            game.setImage(Base64.getEncoder().encode(file.getBytes()));
            game.setExt(Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1)).toString());

            //model.addAttribute("img", "data:image/" + game.getExt() + ";base64," + new String(game.getImage()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Processing new game: " + game);

        return "redirect:/orders/current";
    }
}
