package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleService roleService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
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
    public void save(User user, List<String> roleNames) {
        Set<Role> roles = new HashSet<>(roleService.findByNames(roleNames));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
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
    public void update(User user, List<String> roleNames) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Обновляем основные поля
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setDepartment(user.getDepartment   ());

        // Обновляем пароль, если он был изменен
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Преобразуем имена ролей в объекты Role
        if (roleNames != null && !roleNames.isEmpty()) {
            Set<Role> roles = roleNames.stream()
                    .map(roleService::findByRoleName)
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
        }
        userRepository.save(existingUser);
    }


}
