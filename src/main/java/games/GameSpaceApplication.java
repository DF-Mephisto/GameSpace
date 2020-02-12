package games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GameSpaceApplication {

    public static void main(String[] args) {

        ApplicationContext ac = SpringApplication.run(GameSpaceApplication.class, args);
    }
}
