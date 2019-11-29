package games.data;

import games.Game;
import org.springframework.data.repository.CrudRepository;


public interface GameRepository extends CrudRepository<Game, Long> {

}
