package games.web;

import games.data.UserRepository;
import games.entity.User;
import games.security.SecurityConfig;
import games.security.UserRepositoryUserDetailsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    private List<User> users;

    @BeforeEach
    void setUp()
    {
        users = Arrays.asList(new User("user1", "123456", "ROLE_USER"),
                                         new User("user2", "234567", "ROLE_USER"));

        Mockito.when(userRepo.findAllExceptRole(anyString(), any())).thenReturn(users);
        Mockito.when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(users.get(0)));
    }

    @Test
    public void testUsersPage() throws Exception{
        mockMvc.perform(get("/users/").secure(true)).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Users:")))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", Matchers.hasSize(2)));
    }

    @Test
    public void testLockUser() throws Exception{
        String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        mockMvc.perform(post("/users/{id}/lock/", 0).param("page", "0").secure(true)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(redirectedUrl("/users?page=0"));

        assertThat(users.get(0).isAccountNonLocked()).isEqualTo(false);
    }
}
