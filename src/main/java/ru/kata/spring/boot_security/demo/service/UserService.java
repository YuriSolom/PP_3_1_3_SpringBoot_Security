package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public List<User> getAllUsers();

    public void addUser(User user);

    public User getUser(long id);

    public void updateUser(User user);

    public void deleteUser(User user);

    public User findByUsername(String username);

    public UserDetails loadUserByUsername(String username);
}
