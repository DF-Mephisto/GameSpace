package games.forms;

import games.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProfileForm {

    @NotBlank
    @Size(min=1, max=50, message = "Name must be between 1 and 50 length long")
    String username;

    @NotBlank
    @Size(min=1, max=50, message = "Password must be between 1 and 50 length long")
    String password;

    String oldPassword;
}
