package ru.itmentor.spring.boot_security.demo.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.RoleType;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User(
                "Zhopa",
                "Pisya@v.popy",
                18,
                "123456",
                new ArrayList<>()
        ));
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("selectedRoles", new ArrayList<String>());
        return "user-create";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") List<Long> roleIds) {
        List<Role> roles = roleIds.stream()
                .map(roleService::getRoleById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }



    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") List<String> roles) {
        userService.updateUserWithRoles(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.getUserByID(id);
        List<Role> allRoles = Arrays.stream(RoleType.values())
                .map(rt -> new Role(rt)) // user можно временно null
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "user-edit";
    }
}
