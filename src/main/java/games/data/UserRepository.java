package games.data;

import games.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRole(String role);

    @Query("select u from User u where u.role <> :role")
    List<User> findAllExceptRole(@Param("role") String role, Pageable pageable);

    @Query("select count(u) from User u where u.role <> :role")
    Long countAllExceptRole(@Param("role") String role);
}
