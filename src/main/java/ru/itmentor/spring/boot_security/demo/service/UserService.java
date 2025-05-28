package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public Optional<User> getUserByID(Long id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User updatedUser) {
        if (userRepository.existsById(updatedUser.getId())) {
            userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found with id " + updatedUser.getId());
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void saveUserWithRoles(User user, List<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(name -> roleService.getRoleByAuthority(name))
                .collect(Collectors.toList());
        user.setRoles(roles);
        saveUser(user);
    }

    public void updateUserWithRoles(User user, List<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(name -> roleService.getRoleByAuthority(name))
                .collect(Collectors.toList());
        user.setRoles(roles);
        updateUser(user);
    }
}
