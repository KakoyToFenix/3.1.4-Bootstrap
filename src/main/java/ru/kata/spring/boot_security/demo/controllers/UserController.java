package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUserPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findByUsername(userDetails.getUsername()));
        return "user/user";
    }
//    @GetMapping("/user")
//    public String showUserPage(Authentication authentication, Model model) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User user = userService.findByUsername(userDetails.getUsername());
//        model.addAttribute("user", user);
//        return "user";
//    }
}

