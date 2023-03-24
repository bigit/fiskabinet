package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.UserService;
import ru.antelit.fiskabinet.service.OrgService;

import java.util.List;

@Controller
public class KabinetController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @GetMapping("/home")
    public String home(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo = userService.findUser(userDetails.getUsername());
        model.addAttribute("user", userInfo);

        List<Organization> organizations = orgService.getUserOrganizations(userInfo);
        model.addAttribute("organizations", organizations);
        return "home";
    }

    @PostMapping("/home")
    public String postHome() {
        return "redirect:home";
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:home";
    }
}
