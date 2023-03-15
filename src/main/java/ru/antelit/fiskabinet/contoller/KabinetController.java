package ru.antelit.fiskabinet.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KabinetController {

    @GetMapping( "/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home")
    public String postHome() {
        return "redirect:home";
    }

    @GetMapping("/log")
    public String showLogin() {
        return "login";
    }
}
