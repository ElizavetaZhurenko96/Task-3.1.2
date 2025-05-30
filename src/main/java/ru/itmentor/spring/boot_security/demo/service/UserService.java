package ru.itmentor.spring.boot_security.demo.service;
import ru.itmentor.spring.boot_security.demo.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    UserDto getUserByName(String name);

    List<UserDto> getAllUsers();

    void updateUser(UserDto updatedUserDto, Long id);

    void deleteUser(Long id);
}
