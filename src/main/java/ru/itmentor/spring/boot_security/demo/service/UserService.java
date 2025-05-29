package ru.itmentor.spring.boot_security.demo.service;
import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    Optional<User> getUserByName(String name);

    Optional<User> getUserByID(Long id);

    List<User> getAllUsers();

    void updateUser(User updatedUser);

    void deleteUser(Long id);
}
