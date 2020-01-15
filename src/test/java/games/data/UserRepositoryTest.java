package games.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.Assert;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Test
    void repoIsNotNull()
    {
        Assert.assertNotNull(userRepo);
    }

    @Test
    void testUserRole()
    {
        Assert.assertEquals("ROLE_ADMIN", userRepo.findByUsername("admin").getRole());
    }
}
