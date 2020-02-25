package games.data;

import games.entity.Game;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface GameRepository extends CrudRepository<Game, Long> {
    List<Game> findAll(Pageable pageable);
    List<Game> findByNameIgnoreCaseContaining(String name, Pageable pageable);
    Long countByNameIgnoreCaseContaining(String name);
}
