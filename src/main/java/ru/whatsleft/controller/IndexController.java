package ru.whatsleft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.whatsleft.domain.User;
import ru.whatsleft.repository.RoleRepository;
import ru.whatsleft.service.UserService;
import ru.whatsleft.utility.SecurityUtility;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class IndexController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public IndexController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @RequestMapping("")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        return "index";
    }

    @RequestMapping("login")
    public String login() {
        return "index";
    }

    @RequestMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfilePost(HttpServletRequest request, @ModelAttribute("name") String name, @ModelAttribute("newPassword") String password, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        user.setName(name);
        if (!password.isEmpty()) {
            String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
            user.setPassword(encryptedPassword);
        }
        user = userService.save(user);
        model.addAttribute("user", user);
        model.addAttribute("updateSuccessful", true);
        return "profile";
    }
}
