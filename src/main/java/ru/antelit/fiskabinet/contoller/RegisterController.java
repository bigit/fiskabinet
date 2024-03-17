package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.UserService;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String show(Model model) {
        if (!model.containsAttribute("userInfo")) {
            model.addAttribute("userInfo", new UserInfo());
        }
        return "register";
    }

    @SuppressWarnings("unused")
    @PostMapping("/register")
    public String register(@Valid UserInfo userInfo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userInfo = userService.createUser(userInfo);
        model.addAttribute("register", true);
        return "login";
    }
}
