package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MyController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping("/user")
    public String showUser(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return ("user");
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "users";
    }

    @GetMapping("/admin/user/{id}")
    public String showUser(@PathVariable long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user",user);
        return "user";
    }

    @RequestMapping("/admin/add_new_user")
    public String addNewUser(Model model) {
        User user = new User();
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "new-user";
    }

    @PostMapping("/admin/save_user")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(name = "selectedRoles", required = false) List<String> selectedRoles) {
        if(selectedRoles != null) {
            user.setRoles(selectedRoles.stream().map(role -> roleService.findByName(role)).collect(Collectors.toSet()));
        }
        if (user.getId() != 0) {
            userService.updateUser(user);
        } else {
            userService.addUser(user);
        }
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/edit_user")
    public String updateUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "edit-user";
    }
    @RequestMapping("/admin/pre-delete_user")
    public String preDeleteUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        List<Role> allRole = roleService.getAllRoles();
        model.addAttribute("allRoles", allRole);
        model.addAttribute("user", user);
        return "pre-delete-user";
    }

    @PostMapping("/admin/delete_user")
    public String deleteUser(@RequestParam("userId") long id) {
        User user = userService.getUser(id);
        userService.deleteUser(user);
        return "redirect:/admin/users";
    }
}
