package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "users";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user",user);
        return "user";
    }

    @RequestMapping("/add_new_user")
    public String addNewUser(Model model) {
        User user = new User();
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "new-user";
    }

    @PostMapping("/save_user")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(name = "selectedRoles", required = false) List<String> selectedRoles) {
        if(selectedRoles != null) {
            user.setRoles(selectedRoles.stream().map(role -> roleService.findByName(role)).collect(Collectors.toSet()));
        }
        User user1 = userService.getUser(user.getId());
        if(user1 != null) {
            if (!user.getPassword().equals(user1.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userService.updateUser(user);
        }
         else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addUser(user);
        }
        return "redirect:/admin/users";
    }

    @RequestMapping("/edit_user")
    public String updateUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "edit-user";
    }
    @RequestMapping("/pre-delete_user")
    public String preDeleteUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "pre-delete-user";
    }

    @PostMapping("/delete_user")
    public String deleteUser(@RequestParam("userId") long id) {
        User user = userService.getUser(id);
        userService.deleteUser(user);
        return "redirect:/admin/users";
    }
}
