package games.data;

import games.entity.Game;
import games.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByUser_id(Long user_id, Pageable pageable);
    List<Order> findAll(Pageable pageable);
    Long countByUser_id(Long user_id);
}
