package ru.antelit.fiskabinet.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String show() {
        return "under_construct";
    }
}
