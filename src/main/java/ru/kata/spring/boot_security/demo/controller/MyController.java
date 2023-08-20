package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;


@Controller
public class MyController {
    @Autowired
    UserService userService;

    @GetMapping("/user")
    public String showUser(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return ("user");
    }

    @GetMapping("/admin")
    public String showAdmin(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return ("admin");
    }

    @RequestMapping("/admin/users")
    public String showUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "users-list";
    }

    @RequestMapping("/admin/user/{id}")
    public String showUser(@PathVariable long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute(user);
        return "admin";
    }

    @RequestMapping("/admin/add_new_user")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-create";
    }

    @PostMapping("/admin/save_user")
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getId() != 0) {
            userService.updateUser(user);
        } else {
            userService.addUser(user);
        }
        return "redirect:/admin/users";
    }

    @RequestMapping("/admin/update_user")
    public String updateUser(@RequestParam("userId") long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "user-create";
    }

    @RequestMapping("/admin/delete_user")
    public String deleteUser(@RequestParam("userId") long id) {
        User user = userService.getUser(id);
        userService.deleteUser(user);
        return "redirect:/admin/users";
    }
}
