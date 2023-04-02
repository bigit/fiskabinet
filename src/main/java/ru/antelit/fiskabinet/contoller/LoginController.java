package ru.antelit.fiskabinet.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false, defaultValue = "false") boolean error,
                        @RequestParam(value = "logout", required = false, defaultValue = "false") boolean logout,
                        @RequestParam(value = "register", required = false, defaultValue = "false") boolean register,
                        Model model) {
            model.addAttribute("error", error);
            model.addAttribute("logout", logout);
            model.addAttribute("register", register);
        return "login";
    }

}
