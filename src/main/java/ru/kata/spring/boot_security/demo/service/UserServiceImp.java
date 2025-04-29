package ru.kata.spring.boot_security.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exceptions.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;

@Service
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User getById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    @Override
    public void register(User user, Long roleId) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new  UserAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findById(roleId).get()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public void update(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new  UserAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        // Проверяем, существует ли пользователь
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // Обновляем поля, кроме пароля, если он не пустой
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setDepartment(user.getDepartment());
        // Добавь другие поля, которые нужно обновить

        // Обновляем пароль только если он не пустой
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Обновляем роли
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            Set<Role> updatedRoles = new HashSet<>();
            for (Role role : roles) {
                Optional<Role> existingRole = roleRepository.findByName(role.getRoleName());
                existingRole.ifPresent(updatedRoles::add);
            }
            existingUser.setRoles(updatedRoles);
        }

        // Сохраняем обновленного пользователя
        userRepository.save(existingUser);
    }



}
