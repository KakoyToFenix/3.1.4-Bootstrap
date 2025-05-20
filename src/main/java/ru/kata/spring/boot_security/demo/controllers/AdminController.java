package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exceptions.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String adminPanel(@AuthenticationPrincipal UserDetails userDetails,
                           Model model,
                           @RequestParam(name = "tab", defaultValue = "users") String tab) {
        List<User> allUsers = userService.getAll();
        model.addAttribute("user", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("activeTab", tab);
        return "admin/admin-panel";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user,
                          @RequestParam("checkBoxRoles") List<String> checkBoxRoles) {
        userService.save(user, checkBoxRoles);
        return "redirect:/admin?tab=users";
    }

    @GetMapping("/editUser")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "redirect:/admin";
    }

    @PostMapping("/editUser")
    public String edit(
            @ModelAttribute("user") User user,
            @RequestParam("checkBoxRoles") List<String> checkBoxRoles,
            Model model) {
        try {
            userService.update(user, checkBoxRoles);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.findAll());
        }
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


}
