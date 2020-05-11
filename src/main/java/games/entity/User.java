package games.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String role;

    @Column(name="nonlocked")
    private boolean nonLocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public User(){}

    public User(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
        nonLocked = true;
    }

    public Long getId()
    { return id; }

    public void setId(Long id)
    { this.id = id; }

    public String getUsername()
    { return username; }

    public void setUsername(String username)
    { this.username = username; }

    public String getPassword()
    { return password; }

    public void setPassword(String password)
    { this.password = password; }

    public String getRole()
    { return role; }

    public void setRole(String role)
    { this.role = role; }

    public boolean getNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Arrays.asList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
