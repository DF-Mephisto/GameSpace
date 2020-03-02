package games.web;

import games.data.CommentRepository;
import games.data.UserRepository;
import games.entity.Comment;
import games.entity.Game;
import games.data.GameRepository;
import games.entity.Order;
import games.entity.User;
import games.services.OrderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/app")
@SessionAttributes("order")
public class GameDataController {

    private GameRepository GameRepo;
    private CommentRepository CommRepo;
    private UserRepository UserRepo;
    private OrderFactory OrderFact;

    @Autowired
    public GameDataController(GameRepository GameRepo, OrderFactory OrderFact,
                              CommentRepository CommRepo, UserRepository UserRepo)
    {
        this.GameRepo = GameRepo;
        this.CommRepo = CommRepo;
        this.UserRepo = UserRepo;
        this.OrderFact = OrderFact;
    }

    @ModelAttribute(name = "order")
    public Order order(@CookieValue(value = "items", defaultValue = "") String items)
    {
        return OrderFact.makeOrder(items);
    }

    @GetMapping("/{id}/*")
    public String showGameData(Model model, @PathVariable("id") Long id)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            model.addAttribute("game", res.get());
            model.addAttribute("comment", new Comment());
            return "game";
        }

        return "redirect:/games";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id)
    {
       GameRepo.deleteById(id);

       return "redirect:/games";
    }

    @PostMapping("/comment")
    public String addComment(@Valid @ModelAttribute("comment") Comment comment, BindingResult errors,
                             @RequestParam("id") Long id,
                             @AuthenticationPrincipal User userid)
    {
        Optional<Game> res;
        res = GameRepo.findById(id);
        if (res.isPresent())
        {
            Game game = res.get();
            String gameURI = "app/" + game.getId() + "/" + game.getName();

            if (!errors.hasErrors())
            {
                User user = UserRepo.findById(userid.getId()).get();
                comment.setUser(user);
                comment.setGame(game);

                CommRepo.save(comment);
            }
            return "redirect:/" + gameURI;
        }

        return "redirect:/games";
    }

    @PostMapping("/{gameId}/{gameName}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("gameId") Long gameId, @PathVariable("gameName") String gameName,
                                @PathVariable("commentId") Long commentId)
    {
        String retURI = "app/" + gameId + "/" + gameName;

        CommRepo.deleteById(commentId);

        return "redirect:/" + retURI;
    }
}