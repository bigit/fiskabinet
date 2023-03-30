package ru.antelit.fiskabinet.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotController {

    @GetMapping("/forgot")
    public String index() {
        return "under_construct";
    }
}
