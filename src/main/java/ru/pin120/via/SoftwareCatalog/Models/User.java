package ru.pin120.via.SoftwareCatalog.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements UserDetails {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Некорректный формат email")
    @NotEmpty(message = "Email не может быть пустым")
    @Column(name = "EMAIL")
    private String email;

    @NotEmpty(message = "Логин не может быть пустым")
    @Column(name = "LOGIN")
    private String login;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "PASSWORD")
    private String password;

    @NotEmpty(message = "Имя не может быть пустым")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotEmpty(message = "Фамилия не может быть пустой")
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PROFILE_PHOTO_URL")
    private String profilePhotoUrl;

    @Column(name = "ROLE")
    private String role;

    @ManyToMany
    @JoinTable(
            name = "USER_FAVORITES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "SOFTWARE_ID")
    )
    private Set<Software> favorites = new HashSet<>();

    public User(String email, String password, String firstName, String lastName, String login) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
