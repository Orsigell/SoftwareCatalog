package ru.pin120.via.SoftwareCatalog.Services;

import org.springframework.stereotype.Service;
import ru.pin120.via.SoftwareCatalog.Models.User;
import ru.pin120.via.SoftwareCatalog.Repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return user;
    }
    public User createUser(User user){
        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;
    }
}