package ru.itmentor.spring.boot_security.demo.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
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
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
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

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") List<Long> roleIds) {
        List<Role> roles = roleIds.stream()
                .map(roleService::getRoleById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserByID(id).get();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
