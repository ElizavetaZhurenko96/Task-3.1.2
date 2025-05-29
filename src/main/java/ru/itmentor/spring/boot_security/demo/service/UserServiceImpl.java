package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.findByUsernameWithRoles(name);
    }

    public Optional<User> getUserByID(Long id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles();
    }

    public void updateUser(User updatedUser) {
        if (userRepository.existsById(updatedUser.getId())) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found with id " + updatedUser.getId());
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
