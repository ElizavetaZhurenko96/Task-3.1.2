package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<Role> roles = userDto.getRoles().stream()
                .map(roleType -> roleRepository.findByAuthority(roleType)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleType)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public UserDto getUserByName(String name) {
        User currentUser = userRepository.findByUsernameWithRoles(name).get();
        UserDto currentUserDto = new UserDto();
        currentUserDto.setId(currentUser.getId());
        currentUserDto.setName(currentUser.getName());
        currentUserDto.setEmail(currentUser.getEmail());
        currentUserDto.setAge(currentUser.getAge());
        currentUserDto.setPassword(currentUser.getPassword());
        currentUserDto.setRoles(
                currentUser.getRoles().stream()
                        .map(Role::getAuthorityEnum)
                        .collect(Collectors.toList())
        );
        return currentUserDto;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithRoles()
                .stream().map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(Role::getAuthorityEnum)
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    public void updateUser(UserDto updatedUserDto, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        existingUser.setName(updatedUserDto.getName());
        existingUser.setEmail(updatedUserDto.getEmail());
        existingUser.setAge(updatedUserDto.getAge());
        existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        List<Role> roles = updatedUserDto.getRoles().stream()
                .map(roleType -> roleRepository.findByAuthority(roleType)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleType)))
                .collect(Collectors.toList());
        existingUser.setRoles(roles);
        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
