//package ru.kata.spring.boot_security.demo.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//import ru.kata.spring.boot_security.demo.model.Role;
//import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
//
//import java.util.Optional;
//
//@Service
//public class RoleService {
//
//    RoleRepository roleRepository;
//
//    @Autowired
//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    Optional<Role> findByName(String name) {
//        return roleRepository.findByName(name);
//    }
//}
