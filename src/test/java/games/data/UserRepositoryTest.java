package games.data;

import games.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.Assert;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    private User testUser;
    private User testAdmin;

    @BeforeEach
    void setup()
    {
        testUser = new User("testUser", "123456", "ROLE_USER");
        testAdmin = new User("admin", "admin", "ROLE_ADMIN");

        userRepo.save(testUser);
        userRepo.save(testAdmin);
    }

    @Test
    void repoIsNotNull()
    {
        assertThat(userRepo).isNotNull();

        Iterable<User> users = userRepo.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void testFindByUserName()
    {
        assertThat(userRepo.findByUsername("testUser")).isEqualTo(testUser);
    }

    @Test
    void testFindByRole()
    {
        List<User> users = userRepo.findByRole("ROLE_ADMIN");

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(testAdmin);
    }

    @Test
    void testFindAllExceptRole()
    {
        List<User> users = userRepo
                .findAllExceptRole("ROLE_ADMIN", PageRequest.of(0, 2));

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(testUser);
    }

    @Test
    void testCountAllExceptRole()
    {
        assertThat(userRepo.countAllExceptRole("ROLE_ADMIN")).isEqualTo(1);
    }
}
