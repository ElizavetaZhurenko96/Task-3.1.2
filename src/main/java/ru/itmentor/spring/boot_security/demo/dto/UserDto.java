package ru.itmentor.spring.boot_security.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.RoleType;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private int age;
    private String password;
    private List<RoleType> roles;

    public UserDto(Long id, String name, String email, int age, String password, List<RoleType> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
        this.roles = roles;
    }
}
