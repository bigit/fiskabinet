package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) boolean error,
                        @RequestParam(value = "logout", required = false) boolean logout, Model model) {
        if (error) {
            model.addAttribute("error", error);
        }
        if (logout) {
            model.addAttribute("logout", logout);
        }
        return "login";
    }

}
