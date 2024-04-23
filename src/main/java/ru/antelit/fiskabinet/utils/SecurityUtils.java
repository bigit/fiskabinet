package ru.antelit.fiskabinet.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.service.UserInfoService;

@Component
public class SecurityUtils {

    private final UserInfoService userInfoService;

    @Autowired
    public SecurityUtils(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    public UserInfo getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfoService.getUserByName(userDetails.getUsername());
    }
}
