package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService {
    List<User> getAll();
    User getById(Long id);
    void deleteById(Long id);
    void save(User user, List<String> roleNames);
    User findByUsername(String username);
    void update(User user, List<String> roleName);
}
