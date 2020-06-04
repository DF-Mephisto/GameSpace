package games.services;

import games.data.GameRepository;
import games.entity.Game;
import games.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class OrderFactory {

    private GameRepository GameRepo;

    @Autowired
    public OrderFactory(GameRepository GameRepo)
    {
        this.GameRepo = GameRepo;
    }

    public Order makeOrder(String items)
    {
        Order order = new Order();

        if (!items.equals(""))
        {
            Optional<Game> res;
            List<String> itemList = Arrays.asList(items.split("/"));

            for (String item : itemList)
            {
                res = GameRepo.findById(Long.valueOf(item));
                if (res.isPresent())
                {
                    order.addGame(res.get());
                    order.setTotal_price(order.getTotal_price() + Integer.valueOf(res.get().getPrice()));
                }
            }
        }

        //creditcard template
        order.setCcNumber("4556326939605282");
        order.setCcExpiration("11/11");
        order.setCcCVV("123");

        return order;
    }
}
