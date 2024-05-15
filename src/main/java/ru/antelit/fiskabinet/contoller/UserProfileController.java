package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.UserInfoService;
import ru.antelit.fiskabinet.service.UserService;
import ru.antelit.fiskabinet.utils.SecurityUtils;

@Controller
public class UserProfileController {

    private final UserInfoService userInfoService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Autowired
    public UserProfileController(UserInfoService userInfoService, UserService userService, SecurityUtils securityUtils) {
        this.userInfoService = userInfoService;
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/profile")
    public String index(Model model) {
        UserInfo userInfo = securityUtils.getCurrentUser();
        model.addAttribute("user", userInfo);
        return "profile";
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestParam("first_name") String firstName,
                                           @RequestParam("father_name") String fatherName,
                                           @RequestParam("last_name") String lastName,
                                           @RequestParam("phone") String phone) {

        UserInfo userInfo = userInfoService.getCurrentUser();
        userInfoService.updateAccount(userInfo, firstName, fatherName, lastName, phone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile/password")
    public ResponseEntity<?> changePassword(@RequestParam("password") String newPassword) {
        UserInfo userInfo = securityUtils.getCurrentUser();
        if (userService.updatePassword(userInfo, newPassword)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
