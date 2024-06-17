package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.Software;
import ru.pin120.via.SoftwareCatalog.Models.User;
import ru.pin120.via.SoftwareCatalog.Repositories.SoftwareRepository;
import ru.pin120.via.SoftwareCatalog.Repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SoftwareRepository softwareRepository;
    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with email");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void addFavorite(Long userId, Long softwareId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Software> softwareOptional = softwareRepository.findById(softwareId);

        if (userOptional.isPresent() && softwareOptional.isPresent()) {
            User user = userOptional.get();
            Software software = softwareOptional.get();
            user.getFavorites().add(software);
            userRepository.save(user);
        }
    }

    public void removeFavorite(Long userId, Long softwareId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Software> softwareOptional = softwareRepository.findById(softwareId);

        if (userOptional.isPresent() && softwareOptional.isPresent()) {
            User user = userOptional.get();
            Software software = softwareOptional.get();
            user.getFavorites().remove(software);
            userRepository.save(user);
        }
    }
}
