package games.web;

import games.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/games")
public class ChoseGameController {

    @GetMapping
    public String showGameList(Model model)
    {
       /* List<Game> games = Arrays.asList(
                new Game("Residen Evil 2 Remake", "Survival Horror", "2019", "Capcom", 2000,
                        "Resident Evil 2 is a survival horror game developed and published by Capcom. " +
                            "Players control police officer Leon S. " +
                            "Kennedy and college student Claire Redfield as they attempt to escape from Raccoon City during a zombie apocalypse. " +
                            "It is a remake of the 1998 game Resident Evil 2, the second installment in the Resident Evil video game series."),

                new Game("Grand Theft Auto V", "Action", "2015", "Rockstar Games", 1200,
                        "When a young street hustler, a retired bank robber and a terrifying psychopath find " +
                                "themselves entangled with some of the most frightening and deranged elements of the criminal underworld, " +
                                "the U.S. government and the entertainment industry, they must pull off a series of dangerous heists to survive"),

                new Game("Fallout: New Vegas", "RPG", "2010", "Obsidian Entertainment", 700,
                        "Welcome to Vegas. New Vegas.\n" +
                                "It’s the kind of town where you dig your own grave prior to being shot in the head and left for dead…and that’s before things really get ugly. It’s a town of dreamers and desperados being torn apart by warring factions vying for complete control of this desert oasis. It’s a place where the right kind of person with the right kind of weaponry can really make a name for themselves, and make more than an enemy or two along the way.\n" +
                                "As you battle your way across the heat-blasted Mojave Wasteland, the colossal Hoover Dam, and the neon drenched Vegas Strip, you’ll be introduced to a colorful cast of characters, power-hungry factions, special weapons, mutated creatures and much more. Choose sides in the upcoming war or declare “winner takes all” and crown yourself the King of New Vegas in this follow-up to the 2008 videogame of the year, Fallout 3.\n" +
                                "Enjoy your stay.")
        );

        model.addAttribute("games", games);*/

        return "games";
    }
}
