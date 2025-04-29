package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exceptions.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String allUsers(Model model) {
        List<User> allUsers = userService.getAll();
        model.addAttribute("allUsers", allUsers);
        return "admin/admin";
    }

    @GetMapping("/addUser")
    public String add(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    @PostMapping("/addUser")
    public String add(@ModelAttribute("user") User user,
                           @RequestParam("roleId") Long roleId, Model model) {
        try {
            userService.register(user, roleId);
            return "redirect:/admin";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/add-user";
        }
    }

    @GetMapping("/editUser")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "admin/edit";
    }

    @PostMapping("/editUser")
    public String edit(@ModelAttribute("user") User user, Model model) {
        try {
            userService.update(user);
            return "redirect:/admin";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/edit";
        }
    }

    @GetMapping("/deleteUser")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


}
