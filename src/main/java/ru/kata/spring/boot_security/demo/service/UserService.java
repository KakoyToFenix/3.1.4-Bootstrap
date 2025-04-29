package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User getById(Long id);
    public void deleteById(Long id);
    public void register(User user, Long roleId);
    public User findByUsername(String username);
    public void update(User user);
}
